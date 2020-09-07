package com.yyj.framework.rocketmq.core.producer;

import com.yyj.framework.common.util.log.Log;
import com.yyj.framework.common.util.log.LogFactory;
import com.yyj.framework.rocketmq.core.NameServerAddress;
import com.yyj.framework.rocketmq.core.message.ProducerMsg;
import com.yyj.framework.rocketmq.core.processor.AfterSendAbstractProcessor;
import com.yyj.framework.rocketmq.core.processor.AfterSendLoggerProcessor;
import com.yyj.framework.rocketmq.core.processor.LocalTransactionProcessor;
import com.yyj.framework.rocketmq.core.util.MessageUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.client.producer.selector.SelectMessageQueueByHash;
import org.apache.rocketmq.common.message.Message;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @description RocketMQ的消息生产者类
 */
public class Producer {
    private static final Log logger = LogFactory.getLogger(Producer.class);
    private String groupName;
    private NameServerAddress nameServerAddress;
    private AfterSendAbstractProcessor afterSendProcessor;//消息发送后的处理器
    //发送消息的最大字节数：4M
    private int maxMessageSize = 1024 * 1024 * 4;
    //发送消息超时时间(毫秒)，此超时时间不宜过长或过短
    private int sendMsgTimeout = 20000;
    //消息生产者对象
    private DefaultMQProducer producer;

    //是否支持事务消息
    private Boolean isSupportTransaction = null;
    //本地事务处理器
    private LocalTransactionProcessor localTransactionProcessor;
    private ExecutorService executorService;

    /**
     * 发送顺序消息时的队列选择器
     */
    private MessageQueueSelector orderlyMessageQueueSelector;

    /**
     * 初始化方法
     *
     * @throws Exception
     */
    @PostConstruct
    public void initProducer() throws Exception {
        if (StringUtils.isEmpty(groupName)) {
            throw new RuntimeException("groupName is empty");
        } else if (nameServerAddress == null) {
            throw new RuntimeException("nameServerAddress is null");
        }
        if (isSupportTransaction == null) {
            isSupportTransaction = Boolean.FALSE;
        }

        if (Boolean.FALSE.equals(isSupportTransaction)) {
            this.producer = new DefaultMQProducer(this.groupName);
        } else {
            if (localTransactionProcessor == null) {
                throw new RuntimeException("localTransactionProcessor is null");
            }

            this.producer = new TransactionMQProducer(this.groupName + "_trans");
            this.initExecutorService();
            ((TransactionMQProducer) this.producer).setTransactionListener(localTransactionProcessor);
            ((TransactionMQProducer) this.producer).setExecutorService(this.executorService);
        }

        //有序消息时的消息选择器
        if (orderlyMessageQueueSelector == null) {
            orderlyMessageQueueSelector = new SelectMessageQueueByHash();
        }

        this.producer.setNamesrvAddr(nameServerAddress.getAddresses());
        this.producer.setMaxMessageSize(this.maxMessageSize);
        this.producer.setSendMsgTimeout(this.sendMsgTimeout);
        if (afterSendProcessor == null) {
            afterSendProcessor = new AfterSendLoggerProcessor();//默认的消息发送后处理器
        }
        producer.start();
    }

    /**
     * @param msg 消息内容
     * @return true=发送成功 false=可能失败(可能master成功slave失败；也可能两边都失败；也可能两边都成功了但是等待slave的结果时超时)
     * 对返回结果的说明：
     * 1、返回true，说明消息发送成功，但并不代表一定不会丢失消息，这个主要取决于broker端的配置：单机？异步复制的master？同步复制master？同步刷盘？异步刷盘？
     * 2、返回false，说明消息可能失败，有可能真的是发送失败，也有可能是master成功，但slave失败或等待超时，这种时候消费者是可以接收到消息的，只不过如果master宕机或损坏，有可能会丢失消息
     * @throws RuntimeException 这在多数情况下表示消息是发送失败的，但也有特殊情况：超时，即发送端等待broker端的响应超时，这种情况下，消息是有可能发送成功的
     * @description 发送单个消息的方法，返回true时表示发送成功，但返回false或者抛出异常时并不表示消息一定发送失败
     * 特别说明：
     * 1、因为返回false时也未必就表示消息发送失败，所以，请勿根据发送结果来决定本地事务的提交还是回滚，如果需要这样做，则应该发送事务消息
     * 2、返回false时多数是因为slave不可达，这个时候，如果master宕机并且不可恢复，将有可能导致消息丢失，为了避免此情况，可以把这些消息记录到
     * 日志、数据库中作为备份，这个功能可通过继承{@link AfterSendAbstractProcessor}并覆写相关方法来实现
     * 3、异常抛出，对于TimeOutException可以采取跟上面一步同样的处理方式，对于其他异常，则可认为消息发送失败，打印到日志中即可
     * 4、总体来说，可以做到发送端的消息不丢失，因为通常情况下，有异常抛出或者返回false的情况毕竟是少数，但也可以通过第2步的做法弥补这一缺点
     */
    public <T> Boolean send(ProducerMsg<T> msg) throws RuntimeException {
        Message message = MessageUtil.convertMessage(msg);
        this.validMessage(message, msg);

        Throwable ex = null;
        SendResult sendResult = null;
        long start = System.currentTimeMillis();
        try {
            sendResult = this.producer.send(message);
        } catch (Throwable e) {
            ex = e;
        } finally {
            try {
                afterSendProcessor.executeAfterSend(msg, sendResult, ex, System.currentTimeMillis() - start);
            } catch (Throwable e) {
                logger.error("single_executeAfterSend_Exception", e);
            }
        }
        return this.getReturnValue(sendResult, ex);
    }

    /**
     * 批量发送消息的方法，批量消息不支持有序消息
     * 注意：
     * 1、批量发送消息时，这一批的topic必须一致，但tag可以不一致
     * 2、发送批量消息时，这一批消息的大小不能超过1024KB，即1M
     *
     * @param topic   要发送的消息主题，发送批量消息时，要求topic是同一个
     * @param msgList 要发送的消息
     * @return
     * @see #send(ProducerMsg)
     */
    public <T> Boolean sendBatch(String topic, List<ProducerMsg<T>> msgList) throws RuntimeException {
        int messageSize = 0;
        List<Message> messageList = new ArrayList<>();
        for (ProducerMsg msg : msgList) {
            msg.setTopic(topic);//强制设置同一批次的topic的值为同一个
            Message message = MessageUtil.convertMessage(msg);
            messageSize += message.getBody().length;

            this.validMessage(message, msg);
            if (this.isMessageOverSize(messageSize, maxMessageSize)) {
                throw new RuntimeException("批量消息总体内容过大");
            }
            messageList.add(message);
        }

        Throwable ex = null;
        SendResult sendResult = null;
        long start = System.currentTimeMillis();
        try {
            sendResult = this.producer.send(messageList);
        } catch (Throwable e) {
            ex = e;
        } finally {
            try {
                afterSendProcessor.executeAfterSend(topic, msgList, sendResult, ex, System.currentTimeMillis() - start);
            } catch (Throwable e) {
                logger.error("batch_executeAfterSend_Exception", e);
            }
        }
        return this.getReturnValue(sendResult, ex);
    }

    /**
     * 发送顺序消息的方法
     * 注意：
     * 1、发送顺序消息时必须采用同步发送，比如你连续发了1，2，3。 过了一会，返回结果1失败，2, 3成功。你把1再重新发送1遍，这个时候顺序就乱掉了
     * 2、在broker端，尽管在整体架构上RocketMQ是允许一个topic下可以有多个queue，但是需要保证有序的那一系列消息，必须保证他们都是发送到同一个topic下的同一个queue上，
     * 只有这样，才能保证消费者在消费这个queue中的消息时broker端可以把控其顺序(如通过给queue加锁等方式)
     * 3、在消费端，存在消息预取或者批量消费时，要保证消费时的顺序跟broker中取出时的顺序一致，并且要保证取出的一批消息中，如果前面的消息有消费失败的情况，排在后面的
     * 消息不会被消费
     * 4、想保证消息的有序性，同一系列的消息其msgFlag必须一致，比如有这样一条业务流程：订单创建 -> 支付完成 -> 订单完成，这三个步骤使用的msgFlag必须一致，如可把跟
     * 三个步骤关联的那个订单id作为msgFlag
     * 5、使用有序消息时，需要确保一个topic只能在某一个Master下创建，不能在多个Master都创建同一个topic，这样就会失去负载均衡和FailOver特性，
     * 同时，有序消息，如果一个MessageQueue前面的消息消费失败，那这个MessageQueue后面的消息会被阻塞
     *
     * @param msg 消息体
     * @return
     * @see #send(ProducerMsg)
     */
    public <T> Boolean sendOrderly(ProducerMsg<T> msg) throws RuntimeException {
        Message message = MessageUtil.convertMessage(msg);
        this.validMessage(message, msg);

        Throwable ex = null;
        SendResult sendResult = null;
        long start = System.currentTimeMillis();
        try {
            sendResult = this.producer.send(message, orderlyMessageQueueSelector, msg.getMsgFlag());
        } catch (Throwable e) {
            ex = e;
        } finally {
            try {
                afterSendProcessor.executeAfterSend(msg, sendResult, ex, System.currentTimeMillis() - start);
            } catch (Throwable e) {
                logger.error("orderly_executeAfterSend_Exception", e);
            }
        }
        return this.getReturnValue(sendResult, ex);
    }

    /**
     * 发送事务消息
     * 注意：
     * 1、事务消息和本地事务执行的步骤是：
     * 1.1、设置本地消息处理器
     * 1.2、调用本方法发送事务消息
     * 1.3、RocketMQ调用本地事务处理器执行本地事务，可以根据本地事务执行情况返回不同的状态，告知Broker本地事务是未知/已提交/已回滚
     * 1.4、如果上一步返回的状态是未知，或者返回了其他状态，但是由于某些原因导致Broker没有收到返回的状态，Broker会主动反查生产者，
     * 然后回调用本地事务处理器的检查本地事务方法来对本地事务进行检查
     * 1.5、事务消息，默认3秒之后Broker会反查生产者，也可以在发送消息的时候设置反查时间间隔: MessageConst.PROPERTY_CHECK_IMMUNITY_TIME_IN_SECONDS
     * 1.6、默认情况下，Broker最多只会反查生产者5次，超过这个次数还没获取到结果的话就会做其他处理（目前还未有所处理，后续处理时可能会删除此消息，也有可能会放到死信队列）
     * <p>
     * 2、事务消息不支持有序消息
     * 3、事务消息不支持批量消息
     *
     * @param msg
     * @param <T>
     * @return
     * @throws RuntimeException
     */
    public <T> Boolean sendTransaction(ProducerMsg<T> msg) throws RuntimeException {
        if (!isSupportTransaction) {
            throw new RuntimeException("当前生产者不支持事务，请在Bean配置时设置 isSupportTransaction = true");
        } else if (null == msg.getMsgEvent()) {
            throw new RuntimeException("msgEvent不能为空");
        }

        Message message = MessageUtil.convertMessage(msg);
        this.validMessage(message, msg);

        Throwable ex = null;
        SendResult sendResult = null;
        long start = System.currentTimeMillis();
        try {
            sendResult = this.producer.sendMessageInTransaction(message, msg.getMsgFlag());
        } catch (Throwable e) {
            ex = e;
        } finally {
            try {
                afterSendProcessor.executeAfterSend(msg, sendResult, ex, System.currentTimeMillis() - start);
            } catch (Throwable e) {
                logger.error("transaction_executeAfterSend_Exception", e);
            }
        }
        return this.getReturnValue(sendResult, ex);
    }

    @PreDestroy
    public void shutdownProducer() {
        if (producer != null) {
            producer.shutdown();
        }
    }

    private void validMessage(Message message, ProducerMsg producerMsg) {
        if (StringUtils.isEmpty(message.getTopic())) {
            throw new RuntimeException("topic不能为空");
        } else if (StringUtils.isEmpty(message.getTags())) {
            throw new RuntimeException("tags不能为空");
        } else if (StringUtils.isEmpty(message.getKeys())) {
            throw new RuntimeException("key不能为空");
        } else if (isMessageOverSize(message.getBody().length, maxMessageSize)) {
            throw new RuntimeException("消息体内容过大");
        } else if (null == producerMsg.getMsgEvent() || producerMsg.getMsgEvent() == 0) {
            throw new RuntimeException("msgEvent不能为null或0");
        }
    }

    private boolean isMessageOverSize(int currentSize, int maxSize) {
        return currentSize > maxSize;
    }

    private Boolean getReturnValue(SendResult sendResult, Throwable ex) {
        if (ex != null) {
            throw new RuntimeException(ex.getMessage());
        }
        if (sendResult != null && sendResult.getSendStatus().equals(SendStatus.SEND_OK)) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    private void initExecutorService() {
        if (executorService == null) {
            executorService = new ThreadPoolExecutor(2, 5, 100, TimeUnit.SECONDS, new ArrayBlockingQueue<>(2000), new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    Thread thread = new Thread(r);
                    thread.setName("client-transaction-msg-check-thread");
                    return thread;
                }
            });
        }
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setNameServerAddress(NameServerAddress nameServerAddress) {
        this.nameServerAddress = nameServerAddress;
    }

    public AfterSendAbstractProcessor getAfterSendProcessor() {
        return afterSendProcessor;
    }

    public void setAfterSendProcessor(AfterSendAbstractProcessor afterSendProcessor) {
        if (afterSendProcessor != null) {//为null值时不设置
            this.afterSendProcessor = afterSendProcessor;
        }
    }

    public void setMaxMessageSize(int maxMessageSize) {
        this.maxMessageSize = maxMessageSize;
    }

    public void setSendMsgTimeout(int sendMsgTimeout) {
        this.sendMsgTimeout = sendMsgTimeout;
    }

    public boolean getIsSupportTransaction() {
        return isSupportTransaction;
    }

    public void setIsSupportTransaction(Boolean isSupportTransaction) {
        if (isSupportTransaction == null) {
            throw new RuntimeException("isSupportTransaction不能为NULL");
        } else if (this.isSupportTransaction != null) {
            throw new RuntimeException("初始化已完成，不可再设置isSupportTransaction的值");
        }
        this.isSupportTransaction = isSupportTransaction;
    }

    public LocalTransactionProcessor getLocalTransactionProcessor() {
        return localTransactionProcessor;
    }

    public void setLocalTransactionProcessor(LocalTransactionProcessor localTransactionProcessor) {
        this.localTransactionProcessor = localTransactionProcessor;
    }
}
