package com.example.muse.domain.auth;

import com.example.muse.domain.member.AuthenticationProvider;
import com.example.muse.domain.member.Member;
import com.example.muse.domain.member.MemberRepository;
import com.example.muse.domain.member.Provider;
import com.example.muse.global.security.jwt.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AuthService {
    private final MemberRepository memberRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final List<OAuth2UserInfo> userInfoStrategies;


    @Transactional
    public TokenDto processLogin(Authentication authentication) {
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        OidcUser user = (OidcUser) oauthToken.getPrincipal();

        Provider provider = Provider.valueOf(oauthToken.getAuthorizedClientRegistrationId().toUpperCase());
        OAuth2UserInfo userInfo = userInfoStrategies.stream()
                .filter(strategy -> strategy.getProvider() == provider)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);

        String providerKey = userInfo.getProviderKey(user);
        String nickname = userInfo.getNickname(user);

        Optional<Member> optionalMember = memberRepository.findByAuthenticationProvidersProviderAndAuthenticationProvidersProviderKey(provider, providerKey);
        Member member = optionalMember.orElseGet(() -> signup(provider, providerKey, nickname));

        return login(member);

    }

    @Transactional
    public TokenDto login(Member member) {

        Authentication authentication = new UsernamePasswordAuthenticationToken(member, null, member.getAuthorities());
        String refreshToken = jwtTokenUtil.createRefreshToken(authentication);
        String accessToken = jwtTokenUtil.createAccessToken(authentication);

        return TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Transactional
    public Member signup(Provider provider, String sub, String nickname) {

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