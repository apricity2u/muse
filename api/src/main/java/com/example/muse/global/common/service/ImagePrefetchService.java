package com.example.muse.global.common.service;

import com.example.muse.domain.image.Image;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class ImagePrefetchService {
    private final RestTemplate restTemplate;

    public ImagePrefetchService(RestTemplateBuilder restTemplateBuilder) {

        this.restTemplate = restTemplateBuilder
                .build();
    }

    @Async
    public void prefetchImageCache(Image image) {

        String imageUrl = image.getImageUrl();
        restTemplate.getForEntity(imageUrl, String.class);
    }
}
