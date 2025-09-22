package com.example.muse.global.common.config;

import com.example.muse.global.cache.AppCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;

@Configuration
public class CacheConfig {

    @Bean
    public AppCacheManager cacheManager(RedisCacheManager redisCacheManager) {

        return new AppCacheManager(redisCacheManager);
    }
}
