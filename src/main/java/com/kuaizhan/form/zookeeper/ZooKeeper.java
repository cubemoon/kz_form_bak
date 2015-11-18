/**
 * $Id: ZooKeeper.java 1150 2012-01-31 06:37:43Z adyliu $
 * (C)2011 Sohu Inc.
 */
package com.kuaizhan.form.zookeeper;

import java.io.IOException;
import java.net.SocketAddress;

import org.apache.zookeeper.Watcher;

/** ����Zookeeper�������󣬻�ȡһЩ�ǹ����ӿ�
 * @author adyliu (adyliu@sohu-inc.com)
 * @since 2011-7-17
 */
class ZooKeeper extends org.apache.zookeeper.ZooKeeper {

    public ZooKeeper(String connectString, int sessionTimeout, Watcher watcher, long sessionId, byte[] sessionPasswd) throws IOException {
        super(connectString, sessionTimeout, watcher, sessionId, sessionPasswd);
    }

    public ZooKeeper(String connectString, int sessionTimeout, Watcher watcher) throws IOException {
        super(connectString, sessionTimeout, watcher);
    }

    @Override
    public SocketAddress testableLocalSocketAddress() {
        return super.testableLocalSocketAddress();
    }

    @Override
    public SocketAddress testableRemoteSocketAddress() {
        return super.testableRemoteSocketAddress();
    }

    @Override
    public boolean testableWaitForShutdown(int wait) throws InterruptedException {
        return super.testableWaitForShutdown(wait);
    }
}
