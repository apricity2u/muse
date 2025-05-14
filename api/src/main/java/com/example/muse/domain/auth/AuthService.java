package com.example.muse.domain.auth;

import com.example.muse.domain.member.AuthenticationProvider;
import com.example.muse.domain.member.Member;
import com.example.muse.domain.member.MemberRepository;
import com.example.muse.domain.member.Provider;
import com.example.muse.global.security.jwt.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AuthService extends DefaultOAuth2UserService {
    private final MemberRepository memberRepository;
    private final JwtTokenUtil jwtTokenUtil;

    @Transactional
    public void loginHandler(Authentication authentication) {
        OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
        String providerKey = oidcUser.getAttribute("sub");
        String nickname = oidcUser.getAttribute("nickname");

        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        Provider provider = Provider.valueOf(oauthToken.getAuthorizedClientRegistrationId().toUpperCase());

        Optional<Member> optionalMember = memberRepository
                .findByAuthenticationProvidersProviderAndAuthenticationProvidersProviderKey(provider, providerKey);

        Member member;
        if (optionalMember.isPresent()) {

            member = optionalMember.get();
            login(member);
        } else {


            member = signup(provider, providerKey, nickname);
            login(member);
        }
    }

    private void login(Member member) {
        //TODO: 로그인 로직
    }

    private Member signup(Provider provider, String sub, String nickname) {

        Member member = Member.builder()
                .nickname(nickname)
                .build();

        AuthenticationProvider authenticationProvider =
                AuthenticationProvider.builder()
                        .provider(provider)
                        .providerKey(sub)
                        .build();
        member.addAuthenticationProviders(authenticationProvider);


        return memberRepository.save(member);
    }
}