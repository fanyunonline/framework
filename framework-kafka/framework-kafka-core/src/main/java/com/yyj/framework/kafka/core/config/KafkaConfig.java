package com.yyj.framework.kafka.core.config;

import com.google.common.collect.Maps;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.AbstractMessageListenerContainer;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yangyijun on 2018/10/19.
 */
@Configuration
@EnableKafka
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers:}")
    private String bootstrapServers;
    @Value("${spring.kafka.consumer.enable-auto-commit:}")
    private Boolean autoCommit;
    @Value("${spring.kafka.consumer.auto-commit-interval:}")
    private Integer autoCommitInterval;
    @Value("${spring.kafka.consumer.group-id:}")
    private String groupId;
    @Value("${spring.kafka.consumer.max-poll-records:100}")
    private Integer maxPollRecords;
    @Value("${spring.kafka.consumer.auto-offset-reset:}")
    private String autoOffsetReset;
    @Value("${spring.kafka.consumer.key-serializer:}")
    private String consumerKeySerializer;
    @Value("${spring.kafka.consumer.value-serializer:}")
    private String consumerValueSerializer;
//    @Value("${spring.kafka.listener.ack-mode:}")
//    private Integer ackMode;

    @Value("${spring.kafka.producer.key-serializer:}")
    private String producerKeySerializer;
    @Value("${spring.kafka.producer.value-serializer:}")
    private String producerValueSerializer;
    @Value("${spring.kafka.producer.retries:}")
    private Integer retries;
    @Value("${spring.kafka.producer.acks:}")
    private String acks;
    @Value("${spring.kafka.producer.batch-size:}")
    private Integer batchSize;
    @Value("${spring.kafka.producer.buffer-memory:}")
    private Integer bufferMemory;
    @Value("${spring.kafka.producer.compression-type:gzip}")
    private String compressionType;

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> batchFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
//        factory.setConcurrency(concurrency);
        factory.getContainerProperties().setPollTimeout(1500);
        factory.setBatchListener(true);//@KafkaListener 批量消费  每个批次数量在Kafka配置参数中设置ConsumerConfig.MAX_POLL_RECORDS_CONFIG
        factory.getContainerProperties().setAckMode(AbstractMessageListenerContainer.AckMode.MANUAL_IMMEDIATE);//设置提交偏移量的方式
        return factory;
    }


    public ConsumerFactory<String, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    public Map<String, Object> consumerConfigs() {
        Map<String, Object> propsMap = new HashMap<>(8);
        propsMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        propsMap.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, autoCommit);
//        propsMap.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, autoCommitInterval);
//        propsMap.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, sessionTimeout);
        propsMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        propsMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        propsMap.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        propsMap.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
        propsMap.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxPollRecords);//每个批次获取数
        return propsMap;
    }


    /**
     * 生产者配置信息
     */
    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = Maps.newHashMap();
        props.put(ProducerConfig.ACKS_CONFIG, acks);
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.RETRIES_CONFIG, retries);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, batchSize);
//        props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, bufferMemory);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, compressionType);
        return props;
    }

    /**
     * 生产者工厂
     */
    @Bean
    public ProducerFactory<String, String> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    /**
     * 生产者模板
     */
    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
