package com.yyj.framework.common.util.log;

/**
 * Created by yangyijun on 2017/12/15.
 */
public class LogFactory {
    public static Log getLogger(Class clazz) {
        return new Log(clazz);
    }
}
