package com.example.muse.global.common.config;

import com.example.muse.global.cache.AppCacheManager;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;

import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfig {
    @Bean
    public CaffeineCacheManager caffeineCacheManager() {
        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();
        caffeineCacheManager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(7, TimeUnit.DAYS)
                .maximumSize(10_000));

        return caffeineCacheManager;
    }

    @Bean
    public AppCacheManager cacheManager(RedisCacheManager redisCacheManager, CaffeineCacheManager caffeineCacheManager) {

        return new AppCacheManager(redisCacheManager, caffeineCacheManager);
    }
}
