package com.yyj.framework.common.util.log;

import org.apache.commons.logging.LogFactory;

import java.text.MessageFormat;

/**
 * Created by yangyijun on 2017/12/15.
 */
public class Log {
    private org.apache.commons.logging.Log logger;

    public Log(Class clazz) {
        this.logger = LogFactory.getLog(clazz);
    }

    public void warn(Object obj) {
        logger.warn(obj);
    }

    public void warn(String s, Throwable e) {
        logger.warn(s, e);
    }

    public void warn(String pattern, Object... arguments) {
        logger.warn(format(pattern, arguments));
    }

    public void warn(String pattern, Throwable e, Object... arguments) {
        logger.warn(format(pattern, arguments), e);
    }

    public void info(Object obj) {
        logger.info(obj);
    }

    public void info(String s, Throwable e) {
        logger.info(s, e);
    }

    public void info(String pattern, Object... arguments) {
        logger.info(format(pattern, arguments));
    }

    public void info(String pattern, Throwable e, Object... arguments) {
        logger.info(format(pattern, arguments), e);
    }

    public void debug(Object obj) {
        logger.debug(obj);
    }

    public void debug(String s, Throwable e) {
        logger.debug(s, e);
    }

    public void debug(String pattern, Object... arguments) {
        logger.debug(format(pattern, arguments));
    }

    public void debug(String pattern, Throwable e, Object... arguments) {
        logger.debug(format(pattern, arguments), e);
    }

    public void error(Object obj) {
        logger.debug(obj);
    }

    public void error(String s, Throwable e) {
        logger.error(s, e);
    }

    public void error(String pattern, Object... arguments) {
        logger.error(format(pattern, arguments));
    }

    public void error(String pattern, Throwable e, Object... arguments) {
        logger.error(format(pattern, arguments), e);
    }

    public void trace(Object obj) {
        logger.trace(obj);
    }

    public void trace(String s, Throwable e) {
        logger.trace(s, e);
    }

    public void trace(String pattern, Object... arguments) {
        logger.trace(format(pattern, arguments));
    }

    public void trace(String pattern, Throwable e, Object... arguments) {
        logger.trace(format(pattern, arguments), e);
    }

    ///////////////////////
    // private functions
    ///////////////////////
    private static String format(String pattern, Object... arguments) {
        return MessageFormat.format(pattern, arguments);
    }
}
