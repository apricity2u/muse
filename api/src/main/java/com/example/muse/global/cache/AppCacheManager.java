package com.example.muse.global.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.cache.RedisCacheManager;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
public class AppCacheManager implements CacheManager {
    private final RedisCacheManager redisCacheManager;
    private final Map<String, Cache> caches = new ConcurrentHashMap<>();

    @Override
    public Cache getCache(String name) {

        return caches.computeIfAbsent(name, k -> {
            Cache redisCache = redisCacheManager.getCache(name);
            return new AppCache(name, redisCache);
        });
    }

    @Override
    public Collection<String> getCacheNames() {
        return Collections.unmodifiableSet(caches.keySet());
    }
}
