package com.yyj.framework.es.core.client.tcp;

import com.yyj.framework.common.util.log.Log;
import com.yyj.framework.common.util.log.LogFactory;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.InetAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by yangyijun on 2017/12/01.
 */
@Repository
public class EsTcpClient {

    private static final Log logger = LogFactory.getLogger(EsTcpClient.class);

    private static final Map<String, TransportClient> POOL = new ConcurrentHashMap<>();

    private static TransportClient client;

    @Value("${es.tcp.cluster.name:}")
    private String clusterName;

    @Value("${es.tcp.cluster.nodes:}")
    private String nodes;

    @PostConstruct
    public void connectDefault() {
        client = create(nodes, clusterName);
    }

    @PreDestroy
    public void cleanup() {
        if (client != null) {
            client.close();
        }
        POOL.values().forEach(TransportClient::close);
    }

    public TransportClient getClient() {
        return client;
    }

    public TransportClient getClient(String nodes, String cluster) {
        if (StringUtils.isBlank(nodes) || StringUtils.isBlank(cluster)) {
            return client;
        }
        TransportClient client = POOL.get(cluster + "_" + nodes);
        if (client != null) {
            return client;
        }
        client = create(nodes, cluster);
        POOL.put(cluster + "_" + nodes, client);
        return client;
    }

    ///////////////////////
    // private functions
    ///////////////////////
    private TransportClient create(String nodes, String cluster) {
        TransportClient client = null;
        try {
            String[] hosts = StringUtils.substringBefore(nodes, ":").split(",");
            int port = Integer.parseInt(StringUtils.substringAfter(nodes, ":"));
            Settings settings = Settings
                    .builder()
                    .put("cluster.name", cluster)
                    .put("client.transport.sniff", true)
                    .build();
            client = new PreBuiltTransportClient(settings);
            for (String host : hosts) {
                client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host), port));
//                6.X
//                client.addTransportAddress(new TransportAddress(InetAddress.getByName(host), port));
            }
            logger.info("success to create client of elasticsearch server cluster[{0}] url[{1}]", clusterName, nodes);
            if (client.connectedNodes().size() == 0) {
                logger.warn("the ES client doesn't connect to the ES server cluster[{0}] url[{1}]", clusterName, nodes);
            }
        } catch (Exception e) {
            logger.error("failed to create client of elasticsearch server cluster[{0}] url[{1}].\n", e, clusterName, nodes);
        }
        return client;
    }
}
