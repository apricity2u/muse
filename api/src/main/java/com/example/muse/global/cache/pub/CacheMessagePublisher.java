package com.example.muse.global.cache.pub;

public interface CacheMessagePublisher {
    void publishEvict(String cacheName, Object key);

    void publishClear(String cacheName);
}
