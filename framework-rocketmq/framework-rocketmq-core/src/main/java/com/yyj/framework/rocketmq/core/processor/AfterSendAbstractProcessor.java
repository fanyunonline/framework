package com.yyj.framework.rocketmq.core.processor;

import com.yyj.framework.rocketmq.core.message.ProducerMsg;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.remoting.exception.RemotingTimeoutException;

import java.util.List;

/**
 * @description 消息发送之后的处理器，可以通过继承此类，然后覆盖executeAfterSend方法来进行其他处理(如：存储发送失败的消息到数据库)
 */
public abstract class AfterSendAbstractProcessor {
    /**
     * 消息发送后要执行的逻辑
     *
     * @param msg
     * @param sendResult
     * @param ex
     */
    public abstract <T> void executeAfterSend(ProducerMsg<T> msg, SendResult sendResult, Throwable ex, long costMills);

    /**
     * 批量消息发送后要执行的逻辑
     *
     * @param topic
     * @param msgList
     * @param sendResult
     * @param ex
     */
    public abstract <T> void executeAfterSend(String topic, List<ProducerMsg<T>> msgList, SendResult sendResult, Throwable ex, long costMills);

    /**
     * 确认是否已发送成功
     *
     * @param sendResult
     * @return
     */
    protected final boolean isEnsureSendSuccess(SendResult sendResult) {
        if (sendResult != null && SendStatus.SEND_OK.equals(sendResult.getSendStatus())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 是否Master成功，但Slave失败
     *
     * @param sendResult
     * @return
     */
    protected final boolean isSlaveFail(SendResult sendResult) {
        if (sendResult != null &&
                (SendStatus.FLUSH_SLAVE_TIMEOUT.equals(sendResult.getSendStatus()) || SendStatus.SLAVE_NOT_AVAILABLE.equals(sendResult.getSendStatus()))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断是否超时
     *
     * @param e
     * @return
     */
    protected final boolean isRemoteTimeOut(SendResult sendResult, Throwable e) {
        if (sendResult != null && sendResult.getSendStatus().equals(SendStatus.FLUSH_DISK_TIMEOUT)) {
            return true;
        } else if (e != null && e instanceof RemotingTimeoutException) {
            return true;
        } else {
            return false;
        }
    }
}
