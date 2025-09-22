package com.example.muse.global.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;

import java.util.concurrent.Callable;

@RequiredArgsConstructor
public class AppCache implements Cache {
    private final String name;
    private final Cache globalCache;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object getNativeCache() {
        return globalCache.getNativeCache();
    }

    @Override
    public ValueWrapper get(Object key) {
        return globalCache.get(key);
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        return globalCache.get(key, type);
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        return globalCache.get(key, valueLoader);
    }

    @Override
    public void put(Object key, Object value) {
        globalCache.putIfAbsent(key, value);
    }

    @Override
    public void evict(Object key) {
        globalCache.evict(key);
    }

    @Override
    public void clear() {
        globalCache.clear();
    }
}
