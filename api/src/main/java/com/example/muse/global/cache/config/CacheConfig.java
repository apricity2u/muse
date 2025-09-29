package com.example.muse.global.cache.config;

import com.example.muse.global.cache.AppCacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheManager;

@Configuration
@EnableCaching
public class CacheConfig {


    @Bean
    @Primary
    public AppCacheManager cacheManager(RedisCacheManager redisCacheManager, CaffeineCacheManager caffeineCacheManager) {

        return new AppCacheManager(redisCacheManager, caffeineCacheManager);
    }


}
