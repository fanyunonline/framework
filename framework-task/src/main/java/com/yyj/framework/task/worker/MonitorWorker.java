package com.yyj.framework.task.worker;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by yangyijun on 2018/11/23.
 */
@Component
public class MonitorWorker {
    @Scheduled(cron = "${task.framework.cron}")
    public void runTask() {

    }
}
