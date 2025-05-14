package com.example.muse.global.security.config;

import com.example.muse.domain.auth.CustomOidcUserService;
import com.example.muse.domain.auth.OAuth2FailureHandler;
import com.example.muse.domain.auth.OAuth2SuccessHandler;
import com.example.muse.global.security.handler.CustomAccessDeniedHandler;
import com.example.muse.global.security.handler.JwtAuthenticationEntryPoint;
import com.example.muse.global.security.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final CustomOidcUserService customOidcUserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final OAuth2FailureHandler oAuth2FailureHandler;

    /**
     * 애플리케이션의 HTTP 보안 필터 체인을 구성합니다.
     *
     * CORS를 활성화하고, CSRF, 폼 로그인, HTTP Basic 인증을 비활성화하며, 세션 관리를 무상태로 설정합니다.
     * 모든 OPTIONS 요청과 기타 모든 요청을 허용합니다.
     * JWT 인증 필터를 UsernamePasswordAuthenticationFilter 앞에 추가하고, 인증 및 인가 예외 처리를 구성합니다.
     * OAuth2 로그인을 지원하며, 성공 및 실패 핸들러와 커스텀 OIDC 사용자 서비스를 설정합니다.
     *
     * @return 구성된 SecurityFilterChain 인스턴스
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .formLogin(auth -> auth.disable())
                .httpBasic(auth -> auth.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers(HttpMethod.OPTIONS).permitAll()
//                        .requestMatchers(HttpMethod.POST, SecurityPathConfig.PUBLIC_POST_URLS).permitAll()
//                        .requestMatchers(HttpMethod.GET, SecurityPathConfig.PUBLIC_GET_URLS).permitAll()
//                        .anyRequest().authenticated()
                                .anyRequest().permitAll()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler)
                )
                .oauth2Login(oauth2 -> oauth2
                        .successHandler(oAuth2SuccessHandler)
                        .failureHandler(oAuth2FailureHandler)
                        .userInfoEndpoint(userInfo -> userInfo
                                .oidcUserService(customOidcUserService)
                        )
                );


        return http.build();
    }

}

