package com.kuaizhan.form.zookeeper;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.framework.listen.ListenerContainer;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.utils.EnsurePath;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

/**
 * <p>
 * A utility that attempts to keep the data from a node locally cached. This
 * class will watch the node, respond to update/create/delete events, pull down
 * the data, etc. You can register a listener that will get notified when
 * changes occur.
 * </p>
 *
 * <p>
 * <b>IMPORTANT</b> - it's not possible to stay transactionally in sync. Users
 * of this class must be prepared for false-positives and false-negatives.
 * Additionally, always use the version number when updating data to avoid
 * overwriting another process' change.
 * </p>
 */
public class NodeCache<T> implements Closeable {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final CuratorFramework client;
    private final String path;
    private final EnsurePath ensurePath;
    private final AtomicReference<T> data = new AtomicReference<T>(null);
    private final AtomicReference<State> state = new AtomicReference<State>(State.LATENT);
    private final ListenerContainer<NodeCacheListener> listeners = new ListenerContainer<NodeCacheListener>();
    private final AtomicBoolean isConnected = new AtomicBoolean(true);

    private final NodeCacheDataBuilder<T> builder;

    private final ConnectionStateListener connectionStateListener = new ConnectionStateListener() {
        @Override
        public void stateChanged(CuratorFramework client, ConnectionState newState) {
            if ((newState == ConnectionState.CONNECTED) || (newState == ConnectionState.RECONNECTED)) {
                if (isConnected.compareAndSet(false, true)) {
                    try {
                        reset();
                    } catch (Exception e) {
                        log.error("Trying to reset after reconnection", e);
                    }
                }
            } else {
                isConnected.set(false);
            }
        }
    };

    private final CuratorWatcher watcher = new CuratorWatcher() {
        @Override
        public void process(WatchedEvent event) throws Exception {
            reset();
        }
    };

    private enum State {
        LATENT, STARTED, CLOSED
    }

    private final BackgroundCallback backgroundCallback = new BackgroundCallback() {
        @Override
        public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
            processBackgroundResult(event);
        }
    };


    public NodeCache(CuratorFramework client, String path, NodeCacheDataBuilder<T> builder) {
        this.client = client;
        this.path = path;
        ensurePath = client.newNamespaceAwareEnsurePath(path).excludingLast();
        this.builder = builder;
    }

    /**
     * Start the cache. The cache is not started automatically. You must call
     * this method.
     *
     * @throws Exception
     *             errors
     */
    public void start() throws Exception {
        start(false);
    }

    /**
     * Same as {@link #start()} but gives the option of doing an initial build
     *
     * @param buildInitial
     *            if true, {@link #rebuild()} will be called before this method
     *            returns in order to get an initial view of the node
     * @throws Exception
     *             errors
     */
    public void start(boolean buildInitial) throws Exception {
        Preconditions.checkState(state.compareAndSet(State.LATENT, State.STARTED), "Cannot be started more than once");

        ensurePath.ensure(client.getZookeeperClient());

        client.getConnectionStateListenable().addListener(connectionStateListener);

        if (buildInitial) {
            internalRebuild();
        }
        reset();
    }

    @Override
    public void close() throws IOException {
        if (state.compareAndSet(State.STARTED, State.CLOSED)) {
            listeners.clear();
        }
        client.getConnectionStateListenable().removeListener(connectionStateListener);
    }

    /**
     * Return the cache listenable
     *
     * @return listenable
     */
    public ListenerContainer<NodeCacheListener> getListenable() {
        Preconditions.checkState(state.get() != State.CLOSED, "Closed");

        return listeners;
    }

    /**
     * NOTE: this is a BLOCKING method. Completely rebuild the internal cache by
     * querying for all needed data WITHOUT generating any events to send to
     * listeners.
     *
     * @throws Exception errors
     */
    public void rebuild() throws Exception {
        Preconditions.checkState(state.get() == State.STARTED, "Not started");

        internalRebuild();

        reset();
    }

    /**
     * Return the current data. There are no guarantees of accuracy. This is
     * merely the most recent view of the data. If the node does not exist, this
     * returns null
     *
     * @return data or null
     */
    public T getCurrentData() {
        return data.get();
    }

    private void reset() throws Exception {
        //System.out.printf("state is %s,  isConnect is %s.\n", state.get(), isConnected);
        if ((state.get() == State.STARTED) && isConnected.get()) {
            client.checkExists().usingWatcher(watcher).inBackground(backgroundCallback).forPath(path);
        }
    }

    private void internalRebuild() throws Exception {
        try {
            Stat stat = new Stat();
            byte[] bytes = client.getData().storingStatIn(stat).forPath(path);
            data.set(this.builder.build(bytes));
        } catch (KeeperException.NoNodeException e) {
            data.set(null);
        }
    }

    private void processBackgroundResult(CuratorEvent event) throws Exception {
        switch (event.getType()) {
        case GET_DATA: {
            if (event.getResultCode() == KeeperException.Code.OK.intValue()) {
                T childData = this.builder.build(event.getData());
                setNewData(childData);
            }
            break;
        }

        case EXISTS: {
            if (event.getResultCode() == KeeperException.Code.NONODE.intValue()) {
                setNewData(null);
            } else if (event.getResultCode() == KeeperException.Code.OK.intValue()) {
                client.getData().usingWatcher(watcher).inBackground(backgroundCallback).forPath(path);
            }
            break;
        }
        default:
            throw new IllegalArgumentException("doesn't support this event");
        }
    }

    private void setNewData(T newData) throws InterruptedException {
        T previousData = data.getAndSet(newData);
        if (!Objects.equal(previousData, newData)) {
            listeners.forEach(new Function<NodeCacheListener, Void>() {
                @Override
                public Void apply(NodeCacheListener listener) {
                    try {
                        listener.nodeChanged();
                    } catch (Exception e) {
                        log.error("Calling listener", e);
                    }
                    return null;
                }
            });

        }
    }
}
