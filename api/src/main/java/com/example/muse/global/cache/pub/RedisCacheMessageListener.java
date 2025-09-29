package com.example.muse.global.cache.pub;

import com.example.muse.global.cache.pub.dto.CacheMessageDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisCacheMessageListener implements MessageListener {
    private final CaffeineCacheManager caffeineCacheManager;
    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {

        try {
            String body = new String(message.getBody());
            CacheMessageDto msg = objectMapper.readValue(body, CacheMessageDto.class);
            Cache localCache = caffeineCacheManager.getCache(msg.getCacheName());

            if (localCache == null) {
                return;
            }

            switch (msg.getAction()) {
                case "EVICT" -> {
                    if (msg.getKey() == null) {
                        localCache.clear();
                    } else {
                        localCache.evict(msg.getKey());
                    }
                }

                case "CLEAR" -> localCache.clear();
            }
        } catch (Exception ignored) {
        }
    }
}
