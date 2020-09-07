package com.yyj.framework.hbase.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 启动类
 */
@ComponentScan(basePackages =
        {
                "com.yyj.framework.common.core",
                "com.yyj.framework.hbase.core",
                "com.yyj.framework.hbase.admin"
        })
@SpringBootApplication
public class HBaseAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(HBaseAdminApplication.class, args);
    }
}
