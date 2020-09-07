package com.yyj.framework.es.core.client.rest;

import com.yyj.framework.common.util.log.Log;
import com.yyj.framework.common.util.log.LogFactory;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by yangyijun on 2017/12/01.
 */
@Configuration
public class EsRestClientConfig {

    private static final Log logger = LogFactory.getLogger(EsRestClientConfig.class);

    @Value("${es.http.nodes:}")
    private String nodes;

    @Bean
    public RestClientBuilder restClient() {
        return EsRestClientHelper.getRestClientBuilder(nodes);
    }

    @Bean(name = "highLevelClient")
    public RestHighLevelClient highLevelClient(@Autowired RestClientBuilder restClientBuilder) {
        RestHighLevelClient restClient = EsRestClientHelper.getRestHighLevelClient(restClientBuilder);
        logger.info("success to create elasticsearch rest client url[{0}]", nodes);
        return restClient;
    }

}
