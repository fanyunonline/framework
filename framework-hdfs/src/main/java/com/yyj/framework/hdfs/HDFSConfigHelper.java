package com.yyj.framework.hdfs;

import org.apache.hadoop.conf.Configuration;

/**
 * Created by yangyijun on 2019/5/20.
 */
public final class HDFSConfigHelper {
    public static final Configuration getConfig() {
        Configuration config = new Configuration();
//        config.set("fs.defaultFS", "hdfs://192.168.1.16:9000");
//        config.set("fs.defaultFS", "hdfs://192.168.1.16:9000");
//		config.set("dfs.nameservices", "ns1");
//		config.set("dfs.ha.namenodes.ns1", "nn1,nn2");
//		config.set("dfs.namenode.rpc-address.ns1.nn1", "cdh001:9000");
//		config.set("dfs.namenode.rpc-address.ns1.nn2", "cdh002:9000");
//		config.set("dfs.client.failover.proxy.provider.ns1", "org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider");
        return config;
    }
}
