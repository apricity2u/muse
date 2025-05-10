package com.example.muse.global.security.jwt;


import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenUtil jwtTokenUtil;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            String token = getTokenFromRequest(request);

            if (StringUtils.hasText(token) && jwtTokenUtil.validateToken(token)) {

                Authentication authentication = jwtTokenUtil.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (JwtException e) {
            throw new InsufficientAuthenticationException("유효하지 않는 JWT 토큰입니다.");
        }
            filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {

        return Optional.ofNullable(request.getHeader("Authorization"))
                .filter(bearerToken -> bearerToken.startsWith("Bearer "))
                .map(bearerToken -> bearerToken.substring(7))
                .orElse(null);
    }
}

