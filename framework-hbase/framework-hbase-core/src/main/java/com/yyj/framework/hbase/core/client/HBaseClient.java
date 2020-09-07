package com.yyj.framework.hbase.core.client;

import com.yyj.framework.common.util.log.Log;
import com.yyj.framework.common.util.log.LogFactory;
import com.yyj.framework.hbase.core.conf.HBaseConf;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HConstants;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by yangyijun on 2018/2/5.
 */
@Repository
public class HBaseClient {

    private static final Log logger = LogFactory.getLogger(HBaseClient.class);
    private static final Map<String, Connection> POOL = new ConcurrentHashMap<>();

    private static Connection conn;
    private static Configuration conf;

    @PostConstruct
    public void connectDefault() {
        this.connect(HBaseConf.create());
    }

    @PreDestroy
    public void cleanup() {
        close(conn);
        POOL.values().forEach((connection) -> close(connection));
    }

    public void connect(Configuration conf) {
        try {
            this.conf = conf;
            conn = ConnectionFactory.createConnection(conf);
            logger.info("Success to connected with the HBase server.");
        } catch (IOException e) {
            logger.error("Failed to connect with the HBase server.\n", e);
        }
    }

    /**
     * Get default connection.
     *
     * @return
     */
    public Connection getConnection() {
        return conn;
    }

    /**
     * Get or create connection with url.
     *
     * @param url hbase://192.168.1.16,192.168.1.17,192.168.1.18:2181
     * @return
     */
    public Connection getConnection(String url) {
        Connection conn = POOL.get(url);
        if (conn != null && !conn.isClosed()) {
            return conn;
        }
        conn = create(url);
        POOL.put(url, conn);
        return conn;
    }

    ///////////////////////
    // private functions
    ///////////////////////
    private Connection create(String url) {
        Connection conn = null;
        try {
            if (StringUtils.isBlank(url)) {
                return conn;
            }
            if (StringUtils.contains(url, "://")) {
                url = url.split("://")[1];
            }
            String hosts = StringUtils.substringBefore(url, ":");
            String port = StringUtils.substringAfter(url, ":");
            Configuration conf = HBaseConf.create();
            conf.set(HConstants.ZOOKEEPER_QUORUM, hosts);
            conf.set(HConstants.ZOOKEEPER_CLIENT_PORT, port);
            conf.set(HConstants.ZOOKEEPER_ZNODE_PARENT, "/hbase");
            conf.setInt(HConstants.HBASE_RPC_TIMEOUT_KEY, 200000);
            conf.setInt(HConstants.HBASE_CLIENT_SCANNER_TIMEOUT_PERIOD, 200000);
            conf.setInt(HConstants.HBASE_CLIENT_OPERATION_TIMEOUT, 30000);
            conn = ConnectionFactory.createConnection(conf);

            logger.info("Success to connected with HBase server url[{0}]", url);
        } catch (Exception e) {
            logger.error("Failed to connect with HBase server url[{0}].\n", e, url);
        }
        return conn;
    }

    private void close(Connection conn) {
        try {
            if (conn == null) {
                return;
            }
            conn.close();
        } catch (IOException e) {
            // ignore
            e.printStackTrace();
        }
    }
}
