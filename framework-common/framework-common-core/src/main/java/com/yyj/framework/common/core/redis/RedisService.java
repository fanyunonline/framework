package com.yyj.framework.common.core.redis;

/**
 * Created by yangyijun on 2018/7/2.
 */
public interface RedisService {

    <T> T get(String key);

    <T> boolean put(String key, T value);

    <T> boolean put(String key, T value, int timeout);
}
