package com.yyj.framework.common.util.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.TimeUnit;

/**
 * Created by yangyijun on 2018/10/22.
 */
public class CacheFactory {

    public static final long TIME_OUT_MINUTES = 10L;

    public static <K, V> LoadingCache<K, V> create(CacheLoader loader) {
        return create(loader, TIME_OUT_MINUTES);
    }

    public static <K, V> LoadingCache<K, V> create(CacheLoader loader, long interval) {
        return create(loader, interval, TimeUnit.MINUTES);
    }

    public static <K, V> LoadingCache<K, V> create(CacheLoader loader, long interval, TimeUnit unit) {
        return CacheBuilder.newBuilder().refreshAfterWrite(interval, unit).build(loader);
    }
}
