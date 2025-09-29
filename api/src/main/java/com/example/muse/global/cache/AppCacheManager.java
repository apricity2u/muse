package com.example.muse.global.cache;

import com.example.muse.global.cache.pub.CacheMessagePublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.data.redis.cache.RedisCacheManager;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
public class AppCacheManager implements CacheManager {
    private final RedisCacheManager redisCacheManager;
    private final CaffeineCacheManager caffeineCacheManager;
    private final CacheMessagePublisher publisher;

    @Override
    public Cache getCache(String name) {

        Cache localCache = caffeineCacheManager.getCache(name);
        Cache globalCache = redisCacheManager.getCache(name);

        return new AppCache(name, globalCache, localCache, publisher);
    }

    @Override
    public Collection<String> getCacheNames() {

        Collection<String> localCacheNames = caffeineCacheManager.getCacheNames();
        Collection<String> globalCacheNames = redisCacheManager.getCacheNames();

        Set<String> names = new HashSet<>();
        names.addAll(localCacheNames);
        names.addAll(globalCacheNames);

        return Collections.unmodifiableSet(names);
    }
}
