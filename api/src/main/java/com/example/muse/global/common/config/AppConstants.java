package com.example.muse.global.common.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppConstants {
    @Value("${DOMAIN}")
    private String domain;
    public static String IMAGE_PREFIX;

    @PostConstruct
    public void init() {
        IMAGE_PREFIX = "https://image." + domain;
    }
}
