package com.yyj.framework.es.core.client.rest;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.yyj.framework.es.core.client.rest.EsRestClientHelper.getRestClientBuilder;
import static com.yyj.framework.es.core.client.rest.EsRestClientHelper.getRestHighLevelClient;

/**
 * Created by yangyijun on 2017/12/01.
 */
@Repository
public class EsRestClient {

    private static final Map<String, RestHighLevelClient> POOL = new ConcurrentHashMap<>();

    public RestHighLevelClient getClient(String ipPorts) {
        RestHighLevelClient restHighLevelClient = POOL.get(ipPorts);
        if (null == restHighLevelClient) {
            restHighLevelClient = getRestHighLevelClient(getRestClientBuilder(ipPorts));
            POOL.put(ipPorts, restHighLevelClient);
        }
        return restHighLevelClient;
    }
}
