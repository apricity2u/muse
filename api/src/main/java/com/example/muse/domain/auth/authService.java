package com.example.muse.domain.auth;

import com.example.muse.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class authService extends DefaultOAuth2UserService{
    private final MemberRepository memberRepository;
}
