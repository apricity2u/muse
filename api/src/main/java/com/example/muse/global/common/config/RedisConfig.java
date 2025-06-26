package com.example.muse.global.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
@EnableCaching
public class RedisConfig {
    @Value("${REDIS_HOST}")
    private String redisHost;

    @Value("${REDIS_PORT}")
    private int redisPort;

    @Value("${REDIS_PASSWORD}")
    private String password;

    @Bean
    public LettuceConnectionFactory lettuceConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(
                redisHost, redisPort);
        redisStandaloneConfiguration.setPassword(password);

        LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory(redisStandaloneConfiguration);
        connectionFactory.setValidateConnection(true);
        connectionFactory.afterPropertiesSet();

        return connectionFactory;
    }

    @Bean
    public CacheManager cacheManager(LettuceConnectionFactory connectionFactory) {

        RedisSerializationContext.SerializationPair<String> keySerializer =
                RedisSerializationContext.SerializationPair
                        .fromSerializer(new StringRedisSerializer());

        RedisSerializationContext.SerializationPair<Object> valueSerializer =
                RedisSerializationContext.SerializationPair
                        .fromSerializer(new GenericJackson2JsonRedisSerializer());

        RedisCacheConfiguration cacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofHours(24))
                .serializeKeysWith(keySerializer)
                .serializeValuesWith(valueSerializer);

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(cacheConfig)
                .build();
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.activateDefaultTyping(
                objectMapper.getPolymorphicTypeValidator(),
                ObjectMapper.DefaultTyping.NON_FINAL
        );

        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(objectMapper);

        template.setValueSerializer(serializer);
        template.setHashValueSerializer(serializer);
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setKeySerializer(new StringRedisSerializer());
        template.afterPropertiesSet();

        return template;
    }
}
