package com.example.muse.global.security.jwt;

import com.example.muse.global.common.exception.CustomJwtException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {

        return request.getRequestURI().equals("/api/auth/reissue");
    }

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
                throw new CustomJwtException();
            }

            UUID memberId;
            try {
                memberId = UUID.fromString(token.getSubject());
            } catch (IllegalArgumentException e) {
                throw new CustomJwtException();
            }

            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(
                            memberId, null, List.of()
                    )
            );
            filterChain.doFilter(request, response);

        } catch (ExpiredJwtException | JwtValidationException e) {
            throw new CustomJwtException("JWT 토큰이 만료되었습니다.");

        } catch (BadJwtException | JwtException | IllegalArgumentException e) {
            throw new CustomJwtException();
        }
    }
}