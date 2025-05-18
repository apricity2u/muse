package com.example.muse.domain.auth.dto;

import lombok.*;

@Data
@Builder
public class TokenDto {
    private final String accessToken;
    private final String refreshToken;
}
