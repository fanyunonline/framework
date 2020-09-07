package com.yyj.framework.common.core.redis;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by yangyijun on 2018/12/01.
 */
@Data
@Component
@ConfigurationProperties(prefix = "spring.redis.custom")
public class RedisConfigProps {
    /**
     * 缓存的过期时间配置
     * key为cacheName，value为失效时间单位秒
     */
    private Map<String, Long> expires;
}
