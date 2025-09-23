package com.example.muse.global.cache.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;

@Configuration
public class GlobalCacheConfig {
    private static final Duration ONE_DAY = Duration.ofDays(1);
    private static final Duration SEVEN_DAYS = Duration.ofDays(7);
    private static final Duration FOREVER = Duration.ZERO;
    private static final long DEFAULT_LOCAL_MAX = 10_000L;


    @Bean
    public RedisCacheManager redisCacheManager(LettuceConnectionFactory connectionFactory) {

        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
                .entryTtl(SEVEN_DAYS);

        Map<String, RedisCacheConfiguration> configMap = new HashMap<>();
        configMap.put("searchBook", defaultConfig.entryTtl(SEVEN_DAYS));
        configMap.put("searchBookChar", defaultConfig.entryTtl(FOREVER));

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaultConfig)
                .withInitialCacheConfigurations(configMap)
                .build();
    }
}
