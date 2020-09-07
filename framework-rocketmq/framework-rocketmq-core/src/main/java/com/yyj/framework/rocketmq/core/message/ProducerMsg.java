package com.yyj.framework.rocketmq.core.message;

/**
 * 发送RocketMQ消息时的实体：Producer Message
 *
 * @param <T>
 */
public class ProducerMsg<T> {
    /**
     * 消息主题：也叫队列名，是定位消息的一级逻辑
     */
    private String topic;
    /**
     * 标签：定位消息的二级逻辑，跟topic一起使用，即同一个topic下可以有多个tag，方便用户灵活细分
     */
    private String tags;
    /**
     * 消息的key：主要是方便消息查找，如，把订单号作为某一条消息的key，就可以轻松的根据订单号来查找跟此订单号相关的消息了，方便业务跟踪
     */
    private String key;
    /**
     * 消息体：消费端将获取此内容
     */
    private T body;
    /**
     * 消息标识，
     * 1、如果是发送有序消息，这个属性就是有序消息的标识，因为希望能有序消费的一系列消息，在消息发送时，这一系列消息的flag必须一致，以确保这一系列的消息能发送到同一个queue上面，从而才能实现消费时的顺序消费
     * 2、如果是发送事务消息，这个属性就是事务消息的标识
     */
    private String msgFlag;
    /**
     * 消息事件(如：支付完成、结算完成等)，事务消息时必须设置，在执行本地事务及回查本地事务状态时需要根据此值来查询不同的事务
     */
    private Integer msgEvent;

    public ProducerMsg() {
    }

    public ProducerMsg(String topic, String tags, String key) {
        this.topic = topic;
        this.tags = tags;
        this.key = key;
    }

    public ProducerMsg(String topic, String tags, String key, T body) {
        this.topic = topic;
        this.tags = tags;
        this.key = key;
        this.body = body;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    public String getMsgFlag() {
        return msgFlag;
    }

    public void setMsgFlag(String msgFlag) {
        this.msgFlag = msgFlag;
    }

    public Integer getMsgEvent() {
        return msgEvent;
    }

    public void setMsgEvent(Integer msgEvent) {
        this.msgEvent = msgEvent;
    }
}
