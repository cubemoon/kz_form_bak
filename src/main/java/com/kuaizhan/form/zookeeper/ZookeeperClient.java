package com.kuaizhan.form.zookeeper;

import static java.lang.String.format;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ZooKeeper Client
 * 
 * @author adyliu (adyliu@sohu-inc.com)
 * @since 2011-5-18
 */
public class ZookeeperClient {
	
	private final static Logger log = LoggerFactory.getLogger(ZookeeperClient.class);

    private static volatile ZooKeeper zookeeper;

    /**
     * Ĭ������£�ͬһ��memcache�����пͻ��˺ͷ�����������ã���ݲ�ͬ�ĸ�·��������֣�Ĭ�ϲ��Ի����ĸ�·����'/talent'.
     */
    static final String defaultRootPath = "/talent";

    static final String defaultTestingRootPath = "/testing";

    /**
     * �ͻ��˳�ʼ����������
     */
    static CountDownLatch latch = new CountDownLatch(1);

    /**
     * ��ȡĬ�ϵ��������ĸ�·��
     * <p>
     * ����˳��Javaϵͳ�����Լ������ļ���:
     * <ol>
     * <li>zookeeper.rootpath</li>
     * <li>config.product</li>
     * <li>config.testing</li>
     * </ol>
     * </p>
     * 
     * @return �������ĸ�·��
     */
    public static String getRootPath() {
        SystemConfig sc = SystemConfig.getInstance();
        String rootPath = sc.getString("zookeeper.rootpath", null);
        if (rootPath != null) {
            return rootPath;
        }
        if (sc.getBoolean("config.product", false)) {
            return "";
        }
        if (sc.getBoolean("config.testing", false)) {
            return defaultTestingRootPath;
        }
        return defaultRootPath;
    }

    private static ZooKeeper buildClient(String fullConnectString) {
        try {
            return new ZooKeeper(fullConnectString, 30000, new SucWatcher());
        } catch (IOException e) {
            throw new RuntimeException("init zookeeper fail.", e);
        }

    }

    static class SucWatcher implements Watcher {

        public void process(WatchedEvent event) {
        	if (log.isDebugEnabled()) {
				log.debug("process(WatchedEvent event)");
				log.debug(event.toString());
				log.debug(event.getState() + " " + event.getType());
			}
            if (event.getState() == KeeperState.SyncConnected) {
                if (latch != null) {
                    latch.countDown();
                }
            } else if (event.getState() == KeeperState.Expired) {
                System.out.println("[SUC-CORE] session expired. now rebuilding...");

                //session expired, may be never happending.
                //close old client and rebuild new client
                close();

                getZooKeeper();
            }
        }
    }

    public static String getFullConnectAddress() {
        final String rootPath = getRootPath();
        final String connectString = SystemConfig.getInstance().getString("zookeeper.ips",//
                "10.11.156.71:2181,10.11.5.145:2181,10.11.5.164:2181,192.168.106.63:2181,192.168.106.64:2181");
        return connectString + rootPath;
    }
    
    /**
     * ��ʼ��һ��Zookeeper��������޷���ʼ�����׳�һ��{@link RuntimeException}
     * <p>
     * һ��ZooKeeper���������Ϊsession
     * expired������ؽ�����˵��÷���Ӧ�û��ɴ�ZooKeeperʵ�����Ӧ��ÿ��ͨ�� �˽ӿڽ��л�ȡ���˽ӿ�û���������⡣
     * </p>
     * 
     * @return ZooKeeper����
     */
    public static org.apache.zookeeper.ZooKeeper getZooKeeper() {
        if (zookeeper == null) {
            synchronized (ZookeeperClient.class) {
                if (zookeeper == null) {
                    latch = new CountDownLatch(1);
                    zookeeper = buildClient(getFullConnectAddress());//���ʧ�ܣ��´λ��гɹ��Ļ��
                    long startTime = System.currentTimeMillis();
                    try {
                        latch.await(30, TimeUnit.SECONDS);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        final String logString = "[SUC-CORE] rootPath: %1s\n"//
                                + "           connectString: %2s\n"//
                                + "           local host: %3s\n"//
                                + "           remote host: %4s\n"//
                                + "           zookeeper session id: %5s\n"//
                                + "           init cost: %6s\n"//
                        ;
                        final SocketAddress remoteAddres = zookeeper.testableRemoteSocketAddress();
                        final String remoteHost = remoteAddres != null ? ((InetSocketAddress) remoteAddres).getAddress().getHostAddress() : "";
                        System.out.println(format(logString, getRootPath(), //1
                                getFullConnectAddress(),//1
                                zookeeper.testableLocalSocketAddress(),//3
                                remoteAddres,//4
                                zookeeper.getSessionId(),//5
                                (System.currentTimeMillis() - startTime) + "(ms) " + remoteHost//6
                        ));
                        latch = null;
                    }
                }
            }
        }
        return zookeeper;
    }

    /**
     * �ر�zookeeper���ӣ��ͷ���Դ
     */
    public static synchronized void close() {
        System.out.println("[SUC-CORE] close");
        if (zookeeper != null) {
            try {
                zookeeper.close();
                zookeeper = null;
            } catch (InterruptedException e) {
                //ignore exception
            }
        }
    }
}
