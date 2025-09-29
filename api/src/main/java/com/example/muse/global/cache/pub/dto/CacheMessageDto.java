package com.example.muse.global.cache.pub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CacheMessageDto {
    private String key;
    private String cacheName;
    private String action;
}
