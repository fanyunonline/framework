package com.yyj.framework.kafka.consumer;


import com.yyj.framework.common.util.log.Log;
import com.yyj.framework.common.util.log.LogFactory;
import com.yyj.framework.kafka.core.consumer.AbstractConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * Created by yangyijun on 2018/6/26.
 */
@Component
public class FrameworkConsumer extends AbstractConsumer {


    private static final Log logger = LogFactory.getLogger(FrameworkConsumer.class);

    @KafkaListener(id = "framework", topicPattern = "${spring.kafaka.consumer.topic}", containerFactory = "batchFactory")
    public void processMessage(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {
        super.doProcess(records, ack);
    }

    @Override
    public void persistMessage(List<ConsumerRecord<?, ?>> records) throws Exception {

    }
}
