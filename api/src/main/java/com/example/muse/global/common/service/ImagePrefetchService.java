package com.example.muse.global.common.service;

import com.example.muse.domain.image.Image;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ImagePrefetchService {
    private final RestTemplate restTemplate = new RestTemplateBuilder().build();

    @Async
    public void prefetchImageCache(Image image) {

        String imageUrl = image.getImageUrl();
        restTemplate.headForHeaders(imageUrl);
    }
}
