package com.example.muse.global.cache.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class LocalCacheConfig {
    private static final Duration ONE_DAY = Duration.ofDays(1);
    private static final long DEFAULT_MAX = 10_000L;


    @Bean
    public CaffeineCacheManager caffeineCacheManager() {

        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();

        caffeineCacheManager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(ONE_DAY)
                .maximumSize(DEFAULT_MAX));

        caffeineCacheManager.registerCustomCache("searchBook",
                Caffeine.newBuilder()
                        .expireAfterWrite(ONE_DAY)
                        .maximumSize(DEFAULT_MAX)
                        .build());

        caffeineCacheManager.registerCustomCache("searchBookChar",
                Caffeine.newBuilder()
                        .maximumSize(DEFAULT_MAX)
                        .build());

        return caffeineCacheManager;
    }
}
