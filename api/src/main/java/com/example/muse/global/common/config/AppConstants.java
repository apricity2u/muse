package com.example.muse.global.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppConstants {
    public static String IMAGE_PREFIX;

    @Value("${DOMAIN}")
    public void setDOMAIN(String DOMAIN) {
        IMAGE_PREFIX = "https://image." + DOMAIN;
    }
}
