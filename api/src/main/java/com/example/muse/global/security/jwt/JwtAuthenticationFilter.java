package com.example.muse.global.security.jwt;

import com.example.muse.domain.member.Member;
import com.example.muse.domain.member.MemberRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenUtil jwtTokenUtil;
    private final MemberRepository memberRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String tokenString = jwtTokenUtil.getAccessTokenFromRequest(request);
            if (!StringUtils.hasText(tokenString)) {
                filterChain.doFilter(request, response);
                return;
            }

            Jwt token = jwtTokenUtil.tokenFrom(tokenString);
            if (!jwtTokenUtil.validateToken(token)) {
                throw new JwtException("유효하지 않은 JWT 토큰입니다.");
            }

            UUID memberId;
            try {
                memberId = UUID.fromString(token.getSubject());
            } catch (IllegalArgumentException e) {
                throw new JwtException("JWT 서브젝트가 올바른 UUID 형식이 아닙니다.", e);
            }

            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new InsufficientAuthenticationException("회원을 찾을 수 없습니다."));

            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(
                            member, null, List.of()
                    )
            );
            filterChain.doFilter(request, response);

        } catch (ExpiredJwtException | JwtValidationException e) {
            throw new InsufficientAuthenticationException("JWT 토큰이 만료되었습니다.", e);

        } catch (BadJwtException e) {
            throw new AuthenticationServiceException("잘못된 JWT 토큰 형식입니다.", e);

        } catch (JwtException | IllegalArgumentException e) {
            throw new AuthenticationServiceException("유효하지 않은 JWT 토큰입니다.", e);
        }
    }
}