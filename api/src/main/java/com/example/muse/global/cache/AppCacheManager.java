package com.example.muse.global.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.cache.RedisCacheManager;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class AppCacheManager implements CacheManager {
    private final RedisCacheManager redisCacheManager;
    private final Map<String, Cache> caches = new HashMap<>();

    @Override
    public Cache getCache(String name) {
        Cache redisCache = redisCacheManager.getCache(name);
        return caches.computeIfAbsent(name, k -> new AppCache(name, redisCache));
    }

    @Override
    public Collection<String> getCacheNames() {
        return Collections.unmodifiableSet(caches.keySet());
    }
}
