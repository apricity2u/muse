package com.example.muse.global.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.data.redis.cache.RedisCacheManager;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class AppCacheManager implements CacheManager {
    private final RedisCacheManager redisCacheManager;
    private final CaffeineCacheManager caffeineCacheManager;
    ;
    private final Map<String, Cache> caches = new ConcurrentHashMap<>();
    private final Duration fallbackLocalTtl = Duration.ofDays(7);
    private final long fallbackLocalMax = 10_000L;

    @Override
    public Cache getCache(String name) {

        return caches.computeIfAbsent(name, this::createCache);
    }

    @Override
    public Collection<String> getCacheNames() {
        return Collections.unmodifiableSet(caches.keySet());
    }

    protected Cache createCache(String name) {

        Cache localCache = caffeineCacheManager.getCache(name);
        Cache globalCache = redisCacheManager.getCache(name);
        if (localCache == null) {
            localCache = new CaffeineCache(
                    name,
                    Caffeine.newBuilder()
                            .expireAfterWrite(fallbackLocalTtl.toMillis(), TimeUnit.MILLISECONDS)
                            .maximumSize(fallbackLocalMax)
                            .build()
            );
        }
        return new AppCache(name, globalCache, localCache);
    }
}
