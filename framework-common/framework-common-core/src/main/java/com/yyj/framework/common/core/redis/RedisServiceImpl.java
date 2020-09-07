package com.yyj.framework.common.core.redis;

import com.yyj.framework.common.util.log.Log;
import com.yyj.framework.common.util.log.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * Created by yangyijun on 2018/7/2.
 */
@Service
public class RedisServiceImpl implements RedisService {

    private static final Log LOG = LogFactory.getLogger(RedisServiceImpl.class);

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public <T> T get(String key) {
        Object result;
        try {
            result = redisTemplate.opsForValue().get(key);
            if (null == result) {
                return null;
            }
            return (T) result;
        } catch (Exception e) {
            LOG.error(String.format("Get [Key:%s] from redis failed!", key), e);
            return null;
        }

    }

    @Override
    public <T> boolean put(String key, T value) {
        try {
            redisTemplate.opsForValue().set(key, value);
        } catch (Exception e) {
            LOG.error(String.format("Set [Key:%s][Value:%s] into redis failed!", key, value), e);
            return false;
        }
        return true;
    }

    @Override
    public <T> boolean put(String key, T value, int timeout) {
        try {
            redisTemplate.opsForValue().set(key, value);
            redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
        } catch (Exception e) {
            LOG.error(String.format("Set [Key:%s][Value:%s][TimeOut:%d] into redis failed!", key, value, timeout), e);
            return false;
        }
        return true;
    }
}
