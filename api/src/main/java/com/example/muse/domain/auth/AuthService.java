package com.example.muse.domain.auth;

import com.example.muse.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService extends DefaultOAuth2UserService {
    private final MemberRepository memberRepository;

    public void login(Authentication member) {
        OidcUser user = (OidcUser) member.getPrincipal();
        log.info("user: {}", user);
    }
}