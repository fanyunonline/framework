package com.yyj.framework.es.admin.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by yangyijun on 2018/5/4.
 */
@SpringBootApplication
@EnableCaching
@ComponentScan({
        "com.yyj.framework.common.core",
        "com.yyj.framework.es.core.client.rest",
        "com.yyj.framework.es.admin.rest"
})
@EnableEurekaClient
@EnableFeignClients
public class EsRestAdminApplication {

    public static void main(String[] args) {
        /**
         * Springboot整合Elasticsearch 在项目启动前设置一下的属性，防止报错
         * 解决netty冲突后初始化client时还会抛出异常
         * java.lang.IllegalStateException: availableProcessors is already set to [4], rejecting [4]
         */
        System.setProperty("es.set.netty.runtime.available.processors", "false");
        SpringApplication.run(EsRestAdminApplication.class, args);
    }
}
