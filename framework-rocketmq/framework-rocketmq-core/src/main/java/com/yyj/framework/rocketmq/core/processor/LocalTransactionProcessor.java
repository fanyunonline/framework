package com.yyj.framework.rocketmq.core.processor;

import com.yyj.framework.common.util.log.Log;
import com.yyj.framework.common.util.log.LogFactory;
import com.yyj.framework.rocketmq.core.message.ConsumerMsg;
import com.yyj.framework.rocketmq.core.util.MessageUtil;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

/**
 * @description 本地事务处理器，子类需要通过重写executeLocalTransaction来执行本地事务、通过重写checkLocalTransaction来检查本地事务
 */
public abstract class LocalTransactionProcessor implements TransactionListener {
    private static final Log logger = LogFactory.getLogger(LocalTransactionProcessor.class);

    @Override
    public final LocalTransactionState executeLocalTransaction(final Message msg, final Object arg) {
        ConsumerMsg consumerMsg;
        try {
            consumerMsg = MessageUtil.convertMessage(msg, false);
        } catch (Throwable ex) {
            logger.error("[RMQ_EXECUTE_TRANS_CONVERT_ERROR] Message = {}", msg.toString(), ex);
            return LocalTransactionState.UNKNOW;
        }

        try {
            logger.debug("[RMQ_EXECUTE_TRANS_START] KEY={} MsgEvent={}", consumerMsg.getKey(), consumerMsg.getMsgEvent());
            Boolean isSuccess = this.executeLocalTransaction(consumerMsg);
            if (isSuccess == null) {
                logger.debug("[RMQ_EXECUTE_TRANS_FINISH] KEY={} MsgEvent={} IsSuccess={}", consumerMsg.getKey(), consumerMsg.getMsgEvent(), isSuccess);
                return LocalTransactionState.UNKNOW;
            } else if (Boolean.TRUE.equals(isSuccess)) {
                logger.debug("[RMQ_EXECUTE_TRANS_FINISH] KEY={} MsgEvent={} IsSuccess={}", consumerMsg.getKey(), consumerMsg.getMsgEvent(), isSuccess);
                return LocalTransactionState.COMMIT_MESSAGE;
            } else {
                logger.debug("[RMQ_EXECUTE_TRANS_FINISH] KEY={} MsgEvent={} IsSuccess={}", consumerMsg.getKey(), consumerMsg.getMsgEvent(), isSuccess);
                return LocalTransactionState.ROLLBACK_MESSAGE;
            }
        } catch (Throwable ex) {
            if (ex instanceof RuntimeException) {//业务异常，认为是主动抛出的异常，将直接回滚消息
                logger.error("[RMQ_EXECUTE_TRANS_RuntimeException] KEY={} MsgEvent={}", consumerMsg.getKey(), consumerMsg.getMsgEvent(), ex);
                return LocalTransactionState.ROLLBACK_MESSAGE;
            } else {
                logger.error("[RMQ_EXECUTE_TRANS_EXCEPTION] KEY={} MsgEvent={}", consumerMsg.getKey(), consumerMsg.getMsgEvent(), ex);
                return LocalTransactionState.UNKNOW;
            }
        }
    }

    @Override
    public final LocalTransactionState checkLocalTransaction(final MessageExt msg) {
        ConsumerMsg consumerMsg;
        try {
            consumerMsg = MessageUtil.convertMessage(msg, true);
        } catch (Throwable ex) {
            logger.error("[RMQ_CHECK_TRANS_CONVERT_ERROR] Message = {}", msg.toString(), ex);
            return LocalTransactionState.UNKNOW;
        }

        try {
            logger.debug("[RMQ_CHECK_TRANS_START] KEY={} MsgEvent={}", consumerMsg.getKey(), consumerMsg.getMsgEvent());
            Boolean isSuccess = this.checkLocalTransaction(consumerMsg);
            if (isSuccess == null) {
                logger.debug("[RMQ_CHECK_TRANS_FINISH] KEY={} MsgEvent={} IsSuccess={}", consumerMsg.getKey(), consumerMsg.getMsgEvent(), isSuccess);
                return LocalTransactionState.UNKNOW;
            } else if (Boolean.TRUE.equals(isSuccess)) {
                logger.debug("[RMQ_CHECK_TRANS_FINISH] KEY={} MsgEvent={} IsSuccess={}", consumerMsg.getKey(), consumerMsg.getMsgEvent(), isSuccess);
                return LocalTransactionState.COMMIT_MESSAGE;
            } else {
                logger.debug("[RMQ_CHECK_TRANS_FINISH] KEY={} MsgEvent={} IsSuccess={}", consumerMsg.getKey(), consumerMsg.getMsgEvent(), isSuccess);
                return LocalTransactionState.ROLLBACK_MESSAGE;
            }
        } catch (Throwable ex) {
            logger.error("[RMQ_CHECK_TRANS_EXCEPTION] KEY={} MsgEvent={}", consumerMsg.getKey(), consumerMsg.getMsgEvent(), ex);
            return LocalTransactionState.UNKNOW;
        }
    }

    /**
     * 执行本地事务
     * 注意：
     * 1、事务成功时返回TRUE，事务失败时返回FALSE，事务状态未知时返回NULL
     * 2、如果本方法抛出了RuntimeException，会认为本地事务执行失败，将直接回滚消息
     * 3、LocalTransactionProcessor对象是可复用的，在executeLocalTransaction方法内部可根据msgEvent来判断是哪种消息，进而知道应该查询哪个地方的本地事务
     *
     * @param msg
     * @param <T>
     * @return
     */
    public abstract <T> Boolean executeLocalTransaction(final ConsumerMsg<T> msg);

    /**
     * 检查本地事务
     * 注意：
     * 1、事务成功时返回TRUE，事务失败时返回FALSE，事务状态未知时返回NULL
     * 2、LocalTransactionProcessor对象是可复用的，在checkLocalTransaction方法内部可根据msgEvent来判断是哪种消息，进而知道应该查询哪个地方的本地事务
     *
     * @param msg
     * @param <T>
     * @return
     */
    public abstract <T> Boolean checkLocalTransaction(final ConsumerMsg<T> msg);
}
