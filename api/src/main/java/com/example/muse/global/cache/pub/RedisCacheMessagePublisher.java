package com.example.muse.global.cache.pub;

import com.example.muse.global.cache.pub.dto.CacheMessageDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;


@RequiredArgsConstructor
public class RedisCacheMessagePublisher implements CacheMessagePublisher {
    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper objectMapper;
    public static final String TOPIC = "app:cache:channel";


    @Override
    public void publishEvict(String cacheName, Object key) {

        CacheMessageDto msg = new CacheMessageDto(key != null ? key.toString() : null, cacheName, "EVICT");
        publish(msg);
    }

    private void publish(CacheMessageDto msg) {
        try {
            String json = objectMapper.writeValueAsString(msg);
            stringRedisTemplate.convertAndSend(TOPIC, json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public void publishClear(String cacheName) {

        CacheMessageDto msg = new CacheMessageDto(null, cacheName, "CLEAR");
        publish(msg);
    }
}
