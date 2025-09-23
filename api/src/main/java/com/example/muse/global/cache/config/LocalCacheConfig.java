package com.example.muse.global.cache.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class LocalCacheConfig {

    private static final Duration ONE_DAY = Duration.ofDays(1);
    private static final Duration SEVEN_DAYS = Duration.ofDays(7);
    private static final Duration FOREVER = Duration.ZERO;
    private static final long DEFAULT_MAX = 10_000L;

    @Bean
    public Map<String, CaffeineCache> localCaches() {

        Map<String, CaffeineCache> cacheMap = new HashMap<>();

        cacheMap.put("searchBook", new CaffeineCache(
                "searchBook",
                Caffeine.newBuilder()
                        .expireAfterWrite(ONE_DAY)
                        .maximumSize(DEFAULT_MAX)
                        .build()
        ));

        cacheMap.put("searchBookChar", new CaffeineCache(
                "searchBookChar",
                Caffeine.newBuilder()
                        .expireAfterWrite(FOREVER)
                        .maximumSize(DEFAULT_MAX)
                        .build()
        ));

        return cacheMap;
    }


    @Bean
    public CaffeineCacheManager caffeineCacheManager(Map<String, CaffeineCache> localCaches) {

        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();
        caffeineCacheManager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(ONE_DAY)
                .maximumSize(DEFAULT_MAX));

        if (localCaches != null && !localCaches.isEmpty()) {
            caffeineCacheManager.setCacheNames(localCaches.keySet());
        }

        return caffeineCacheManager;
    }
}
