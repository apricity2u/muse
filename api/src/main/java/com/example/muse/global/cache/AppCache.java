package com.example.muse.global.cache;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.cache.Cache;

import java.util.concurrent.Callable;

@RequiredArgsConstructor
public class AppCache implements Cache {
    private final String name;
    private final Cache globalCache;
    private final Cache localCache;

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

        T loadedValue = valueLoader.call();
        put(key, loadedValue);
        return loadedValue;

    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {

        ValueWrapper localValueWrapper = localCache.putIfAbsent(key, value);
        if (localValueWrapper != null) {
            return localValueWrapper;
        }

        if (globalCache != null) {
            globalCache.putIfAbsent(key, value);
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
        globalCache.evict(key);
    }

    @Override
    public void clear() {

        localCache.clear();
        globalCache.clear();
    }
}
