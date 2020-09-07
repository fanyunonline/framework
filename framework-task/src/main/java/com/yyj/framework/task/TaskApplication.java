package com.yyj.framework.task;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by yangyijun on 2018/11/23.
 */
@EnableScheduling
@SpringBootApplication
@ComponentScan(
        {
                "com.yyj.framwork.core",
                "com.yyj.framwork.task"
        })
public class TaskApplication {
    public static void main(String[] args) {
        SpringApplication.run(TaskApplication.class, args);
    }
}
