package com.yyj.framework.kafka.core.consumer;

import com.yyj.framework.common.util.log.Log;
import com.yyj.framework.common.util.log.LogFactory;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * Created by yangyijun on 2018/10/19.
 */

@Component
public abstract class AbstractConsumer {
    private static final Log logger = LogFactory.getLogger(AbstractConsumer.class);

    public void doProcess(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {
        try {
            persistMessage(records);
            ack.acknowledge();
        } catch (Exception e) {
            logger.info(e);
        }
    }

    public abstract void persistMessage(List<ConsumerRecord<?, ?>> records) throws Exception;

}
