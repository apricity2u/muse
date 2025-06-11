package com.example.muse.global.common.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import java.util.Set;

public class CustomMultipartResolver extends StandardServletMultipartResolver {
    @Override
    public boolean isMultipart(HttpServletRequest request) {
        
        String method = request.getMethod().toUpperCase();
        String contentType = request.getContentType();

        return Set.of("POST", "PUT", "PATCH")
                .contains(method)
                && contentType != null
                && contentType.toLowerCase().startsWith(MediaType.MULTIPART_FORM_DATA_VALUE);
    }
}
