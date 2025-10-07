package com.example.muse.global.cache.config;

import com.example.muse.global.cache.AppCacheManager;
import com.example.muse.global.cache.pub.CacheMessagePublisher;
import com.example.muse.global.cache.pub.RedisCacheMessageListener;
import com.example.muse.global.cache.pub.RedisCacheMessagePublisher;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

@Configuration
@EnableCaching
@RequiredArgsConstructor
public class CacheConfig {
    private final LettuceConnectionFactory connectionFactory;
    private final ObjectMapper objectMapper;

    @Bean
    @Primary
    public AppCacheManager cacheManager(RedisCacheManager redisCacheManager, CaffeineCacheManager caffeineCacheManager, CacheMessagePublisher publisher) {

        return new AppCacheManager(redisCacheManager, caffeineCacheManager, publisher);
    }

    @Bean
    public CacheMessagePublisher cacheMessagePublisher(StringRedisTemplate stringRedisTemplate) {
        return new RedisCacheMessagePublisher(stringRedisTemplate, objectMapper);
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisCacheMessageListener listener) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(listener, new PatternTopic(RedisCacheMessagePublisher.TOPIC));
        return container;
    }
}
