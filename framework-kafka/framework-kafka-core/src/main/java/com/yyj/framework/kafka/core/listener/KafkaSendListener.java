package com.yyj.framework.kafka.core.listener;

import org.springframework.kafka.support.SendResult;

/**
 * Created by yangyijun on 2018/10/18.
 */
public interface KafkaSendListener {
    void onSuccess(SendResult<String, String> result);

    void onFailed(Throwable throwable);
}
