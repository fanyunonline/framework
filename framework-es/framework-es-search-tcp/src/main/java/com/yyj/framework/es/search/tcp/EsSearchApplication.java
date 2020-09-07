package com.yyj.framework.es.search.tcp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * 启动类
 */
@ComponentScan(basePackages =
        {
                "com.yyj.framework.common.core",
                "com.yyj.framework.es.core.client.tcp",
                "com.yyj.framework.es.search.tcp"
        })
@SpringBootApplication
@EnableCaching
@EnableEurekaClient
@EnableFeignClients
public class EsSearchApplication {
    public static void main(String[] args) {
        /**
         * Springboot整合Elasticsearch 在项目启动前设置一下的属性，防止报错
         * 解决netty冲突后初始化client时还会抛出异常
         * java.lang.IllegalStateException: availableProcessors is already set to [4], rejecting [4]
         */
        System.setProperty("es.set.netty.runtime.available.processors", "false");
        SpringApplication.run(EsSearchApplication.class, args);
    }
}
