package com.kuaizhan.form.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.CuratorFrameworkFactory.Builder;
import org.apache.curator.retry.RetryNTimes;

public class SystemZookeeperClientFactory {

    private static String DEFAULT_ZK_SERVERS = "10.11.156.71:2181,10.11.5.145:2181,10.11.5.164:2181,192.168.106.63:2181,192.168.106.64:2181";

    private static SystemZookeeperClientFactory instance = new SystemZookeeperClientFactory();

    private volatile CuratorFramework client;

    private SystemZookeeperClientFactory() {
    };

    public static SystemZookeeperClientFactory getInstance() {
        return instance;
    }

    public CuratorFramework getZookeeperClient() {
        if (this.client == null) {
            synchronized (this) {
                if (this.client == null) {
                    Builder builder = CuratorFrameworkFactory.builder().connectString(getZookeeperConnectString())
                            .retryPolicy(new RetryNTimes(10, 1000)).connectionTimeoutMs(5000);
                    String namespace = getNamespace();
                    if (namespace != null && !namespace.trim().equals(""))
                        builder.namespace(namespace);

                    client = builder.build();
                    client.start();
                }
            }
        }
        return this.client;
    }

    enum EnvNamespace {
        DEVELOPMENT("talent"), TESTING("testing"), PRODUCT(null);

        private String prefix;

        private EnvNamespace(String prefix) {
            this.prefix = prefix;
        }

        public String value() {
            return this.prefix;
        }
    }

    public String getNamespace() {
        SystemConfig sc = SystemConfig.getInstance();
        String prefix = sc.getString("zookeeper.namespace", null);
        if (prefix != null) {
            return prefix;
        }
        if (sc.getBoolean("config.product", false)) {
            return EnvNamespace.PRODUCT.value();
        }
        if (sc.getBoolean("config.testing", false)) {
            return EnvNamespace.TESTING.value();
        }
        return EnvNamespace.DEVELOPMENT.value();
    }

    public String getZookeeperConnectString() {
        return SystemConfig.getInstance().getString("zookeeper.servers", DEFAULT_ZK_SERVERS);
    }

}
