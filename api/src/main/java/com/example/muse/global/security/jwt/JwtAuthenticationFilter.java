package com.example.muse.global.security.jwt;


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
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenUtil jwtTokenUtil;
    private final MemberRepository memberRepository;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String tokenString = jwtTokenUtil.getAccessTokenFromRequest(request);

        if (!StringUtils.hasText(tokenString)) {

            filterChain.doFilter(request, response);
            return;
        }

        Jwt token = jwtTokenUtil.from(tokenString);
        if (!jwtTokenUtil.validateToken(token)) {

            throw new JwtException("유효하지 않은 JWT 토큰입니다.");
        }

        UUID memberId;
        try {
            memberId = UUID.fromString(token.getSubject());
        } catch (IllegalArgumentException e) {
            throw new JwtException("JWT 서브젝트가 올바른 UUID 형식이 아닙니다.", e);
        }

        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new InsufficientAuthenticationException("회원을 찾을 수 없습니다.")
        );

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(member, null, List.of())
        );

        filterChain.doFilter(request, response);
    }

}

