package com.yyj.framework.es.core.client.rest;

import com.yyj.framework.common.util.log.Log;
import com.yyj.framework.common.util.log.LogFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;

import java.util.Arrays;
import java.util.Objects;

/**
 * Created by yangyijun on 2017/12/01.
 */
public final class EsRestClientHelper {

    private static final Log logger = LogFactory.getLogger(EsRestClientHelper.class);
    private static final int ADDRESS_LENGTH = 2;
    private static final String HTTP_SCHEME = "http";

    public static RestHighLevelClient getRestHighLevelClient(RestClientBuilder restClientBuilder) {
        restClientBuilder.setMaxRetryTimeoutMillis(60000);
//        return new RestHighLevelClient(restClientBuilder.build());
        return new RestHighLevelClient(restClientBuilder);
    }

    public static RestClientBuilder getRestClientBuilder(String ipPorts) {
        HttpHost[] hosts = Arrays.stream(ipPorts.split(","))
                .map(EsRestClientHelper::makeHttpHost)
                .filter(Objects::nonNull)
                .toArray(HttpHost[]::new);
        logger.info("hosts:{0}", Arrays.toString(hosts));
        return RestClient.builder(hosts);
    }

    public static HttpHost makeHttpHost(String s) {
        assert StringUtils.isNotEmpty(s);
        String[] address = s.split(":");
        if (address.length == ADDRESS_LENGTH) {
            String ip = address[0];
            int port = Integer.parseInt(address[1]);
            return new HttpHost(ip, port, HTTP_SCHEME);
        } else {
            return null;
        }
    }
}
