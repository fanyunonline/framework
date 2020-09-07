package com.yyj.framework.rocketmq.core.util;

import com.yyj.framework.common.util.json.JsonUtils;
import com.yyj.framework.rocketmq.core.conts.MessageConst;
import com.yyj.framework.rocketmq.core.message.ConsumerMsg;
import com.yyj.framework.rocketmq.core.message.ProducerMsg;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.common.message.Message;

import java.nio.charset.Charset;

public class MessageUtil {
    private final static String CHARSET = "utf-8";

    /**
     * 消息转换，把 ProducerMsg 转换为 RocketMQ 的 Message
     *
     * @param msg
     * @param <T>
     * @return
     */
    public static <T> Message convertMessage(ProducerMsg<T> msg) {
        Message message = new Message(msg.getTopic(), msg.getTags(), msg.getKey(),
                JsonUtils.toString(msg.getBody()).getBytes(Charset.forName(CHARSET)));
        message.putUserProperty(MessageConst.PROPERTY_MSG_BIZ_KEY, msg.getKey() == null ? "" : msg.getKey());
        if (null != msg.getMsgEvent()) {
            message.putUserProperty(MessageConst.PROPERTY_MSG_EVENT, String.valueOf(msg.getMsgEvent()));
        }
        if (null != msg.getBody()) {
            message.putUserProperty(MessageConst.PROPERTY_MSG_BODY_CLASS, msg.getBody().getClass().getName());
        }
        return message;
    }

    /**
     * 消息转换，把 RocketMQ 的 Message 转换为 ConsumerMsg，如果CMessage中body对应的Class在当前应用找不到，则会把body转换成String
     *
     * @param msg
     * @param isMsgExt
     * @param <T>
     * @return
     */
    public static <T> ConsumerMsg convertMessage(Message msg, boolean isMsgExt) {
        ConsumerMsg consumerMsg = new ConsumerMsg();
        consumerMsg.setTopic(msg.getTopic());
        consumerMsg.setTags(msg.getTags());
        consumerMsg.setKey(msg.getUserProperty(MessageConst.PROPERTY_MSG_BIZ_KEY));

        String msgEvent = msg.getUserProperty(MessageConst.PROPERTY_MSG_EVENT);
        if (StringUtils.isNotEmpty(msgEvent)) {
            consumerMsg.setMsgEvent(Integer.valueOf(msgEvent));
        }
        String bodyClassName = msg.getUserProperty(MessageConst.PROPERTY_MSG_BODY_CLASS);
        if (StringUtils.isEmpty(bodyClassName)) {
            consumerMsg.setBody(msg.getBody());
        } else {
            try {
                Class cls = Class.forName(bodyClassName);
                consumerMsg.setBody(JsonUtils.toBean(msg.getBody(), cls));
            } catch (ClassNotFoundException e) {
                consumerMsg.setBody(msg.getBody());
            }
        }
        return consumerMsg;
    }
}
