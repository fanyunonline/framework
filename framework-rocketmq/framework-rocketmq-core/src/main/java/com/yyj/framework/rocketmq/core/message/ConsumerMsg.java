package com.yyj.framework.rocketmq.core.message;

/**
 * 消费RocketMQ的消息实体：Consume Message
 */
public class ConsumerMsg<T> {
    /**
     * 消息主题：也叫队列名，是定位消息的一级逻辑
     */
    private String topic;
    /**
     * 消息tag
     */
    private String tags;
    /**
     * 消息的key：主要是方便消息查找，如，把订单号作为某一条消息的key，就可以轻松的根据订单号来查找跟此订单号相关的消息了，方便业务跟踪
     */
    private String key;
    /**
     * 消息体：消费端将获取此内容
     */
    private T body = null;
    /**
     * 消息事件(如：支付完成、结算完成等)，事务消息时必须设置，在执行本地事务及回查本地事务状态时需要根据此值来查询不同的事务
     */
    private Integer msgEvent;

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

    public Integer getMsgEvent() {
        return msgEvent;
    }

    public void setMsgEvent(Integer msgEvent) {
        this.msgEvent = msgEvent;
    }
}
