package com.example.muse.global.cache;

import com.example.muse.global.cache.pub.CacheMessagePublisher;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.cache.Cache;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
public class AppCache implements Cache {
    private final String name;
    private final Cache globalCache;
    private final Cache localCache;
    private final CacheMessagePublisher publisher;
    private final ConcurrentHashMap<Object, Object> keyLocks = new ConcurrentHashMap<>();

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object getNativeCache() {
        return this;
    }

    @Override
    public ValueWrapper get(Object key) {

        ValueWrapper localVWrapper = localCache.get(key);
        if (localVWrapper != null) {
            return localVWrapper;
        }

        if (globalCache == null) {
            return null;
        }

        ValueWrapper globalValue = globalCache.get(key);

        if (globalValue != null && globalValue.get() != null) {
            localCache.put(key, globalValue.get());
            return globalValue;
        }

        return null;
    }

    @Override
    public <T> T get(Object key, Class<T> type) {

        ValueWrapper valueWrapper = get(key);
        return valueWrapper != null ? type.cast(valueWrapper.get()) : null;
    }

    @SneakyThrows
    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {

        ValueWrapper valueWrapper = get(key);
        if (valueWrapper != null) {
            return (T) valueWrapper.get();
        }

        Object lock = keyLocks.computeIfAbsent(key, k -> new Object());
        synchronized (lock) {
            try {
                valueWrapper = get(key);
                if (valueWrapper != null && valueWrapper.get() != null) {
                    return (T) valueWrapper.get();
                }

                T loaded = valueLoader.call();
                if (loaded != null) {
                    put(key, loaded);
                }

                return loaded;
            } finally {
                keyLocks.remove(key, lock);
            }
        }
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {

        if (globalCache != null) {
            ValueWrapper existingGlobal = globalCache.putIfAbsent(key, value);
            if (existingGlobal != null && existingGlobal.get() != null) {
                localCache.put(key, existingGlobal.get());
                return existingGlobal;
            }
        }

        ValueWrapper localValueWrapper = localCache.putIfAbsent(key, value);
        if (localValueWrapper != null && localValueWrapper.get() != null) {
            return localValueWrapper;
        }

        return null;
    }

    @Override
    public void put(Object key, Object value) {

        localCache.put(key, value);
        if (globalCache != null) {
            globalCache.put(key, value);
        }
    }

    @Override
    public void evict(Object key) {

        localCache.evict(key);
        if (globalCache != null) {
            globalCache.evict(key);
        }

        if (publisher != null) {
            publisher.publishEvict(name, key);
        }
    }

    @Override
    public void clear() {

        localCache.clear();

        if (globalCache != null) {
            globalCache.clear();
        }

        if (publisher != null) {
            publisher.publishClear(name);
        }
    }
}
