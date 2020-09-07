package com.yyj.framework.kafka.core.producer;

import com.yyj.framework.common.util.log.Log;
import com.yyj.framework.common.util.log.LogFactory;
import com.yyj.framework.kafka.core.listener.KafkaSendListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.List;

/**
 * Created by yangyijun on 2018/10/20.
 */
@Component
public class KafkaProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private static final Log logger = LogFactory.getLogger(KafkaProducer.class);

    /**
     * 向topic中发送消息
     */
    public void send(String topic, String msg, KafkaSendListener listener) {
        kafkaTemplate.send(topic, msg).addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onFailure(Throwable throwable) {
                logger.info("===================kafka send failure");
                listener.onFailed(throwable);
            }

            @Override
            public void onSuccess(SendResult<String, String> result) {
                logger.info("===================kafka send success");
                listener.onSuccess(result);
            }
        });
    }

    /**
     * 向topic中发送消息
     */
    public void send(String topic, List<String> msgs, KafkaSendListener listener) {
        msgs.forEach(msg -> send(topic, msgs, listener));
    }
}
