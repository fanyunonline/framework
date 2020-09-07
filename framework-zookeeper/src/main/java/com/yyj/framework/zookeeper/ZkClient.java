package com.yyj.framework.zookeeper;


import com.yyj.framework.common.util.log.Log;
import com.yyj.framework.common.util.log.LogFactory;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by yangyijun on 2019/6/7.
 */
public class ZkClient {

    private static Log logger = LogFactory.getLogger(ZkClient.class);
    private static Map<String, ZooKeeper> pool = new ConcurrentHashMap<>();

    public ZooKeeper create(String hostPorts, int sessionTimeout, Watcher watcher) {
        ZooKeeper zk = pool.get(hostPorts);
        if (null != zk) {
            return zk;
        } else {
            try {
                zk = new ZooKeeper(hostPorts, sessionTimeout, watcher);
                pool.put(hostPorts, zk);
                logger.info("create zookeeper client success!");
            } catch (IOException e) {
                logger.error("create zookeeper client exception", e);
            }
        }
        return zk;
    }

    public ZooKeeper getClient(String hostPorts, int sessionTimeout, Watcher watcher) {
        return create(hostPorts, sessionTimeout, watcher);
    }
}
