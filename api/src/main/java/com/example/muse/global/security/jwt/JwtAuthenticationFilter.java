package com.example.muse.global.security.jwt;


import com.example.muse.domain.auth.TokenResponseWriter;
import com.example.muse.domain.member.Member;
import com.example.muse.domain.member.MemberRepository;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenUtil jwtTokenUtil;
    private final MemberRepository memberRepository;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String tokenString = getTokenFromRequest(request);

        if (!StringUtils.hasText(tokenString)) {

            filterChain.doFilter(request, response);
            return;
        }

        Jwt token = jwtTokenUtil.from(tokenString);
        if (!jwtTokenUtil.validateToken(token)) {

            throw new JwtException("유효하지 않은 JWT 토큰입니다.");
        }

        Member member = memberRepository.findById(UUID.fromString(token.getSubject())).orElseThrow(
                () -> new InsufficientAuthenticationException("회원을 찾을 수 없습니다.")
        );

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(member, null, List.of())
        );

        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {

        return Optional.ofNullable(request.getHeader(TokenResponseWriter.AUTH_HEADER))
                .filter(bearerToken -> bearerToken.startsWith(TokenResponseWriter.BEARER_PREFIX))
                .map(bearerToken -> bearerToken.substring(7))
                .orElse(null);
    }
}

