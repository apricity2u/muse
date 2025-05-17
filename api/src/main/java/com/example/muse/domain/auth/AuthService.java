package com.example.muse.domain.auth;

import com.example.muse.domain.member.AuthenticationProvider;
import com.example.muse.domain.member.Member;
import com.example.muse.domain.member.MemberRepository;
import com.example.muse.domain.member.Provider;
import com.example.muse.global.security.jwt.JwtTokenUtil;
import com.example.muse.global.security.jwt.TokenRedisService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AuthService {
    private final MemberRepository memberRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final List<OAuth2UserInfo> userInfoStrategies;
    private final TokenRedisService tokenRedisService;
    private final TokenResponseWriter tokenResponseWriter;


    @Transactional
    public Member processLogin(Authentication authentication) {

        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        OAuth2User oauth2User = oauthToken.getPrincipal();

        Provider provider = Provider.valueOf(
                oauthToken.getAuthorizedClientRegistrationId().toUpperCase()
        );
        OAuth2UserInfo userInfo = userInfoStrategies.stream()
                .filter(strategy -> strategy.getProvider() == provider)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);

        String providerKey = userInfo.getProviderKey(oauth2User);
        String nickname = userInfo.getNickname(oauth2User);

        Optional<Member> optionalMember = memberRepository
                .findByAuthenticationProvidersProviderAndAuthenticationProvidersProviderKey(provider, providerKey);


        return optionalMember.orElseGet(
                () -> signup(provider, providerKey, nickname)
        );
    }


    @Transactional
    public TokenDto login(Member member) {

        Jwt refreshToken = jwtTokenUtil.createRefreshToken(member);
        Jwt accessToken = jwtTokenUtil.createAccessToken(member);

        String refreshTokenJti = jwtTokenUtil.getJtiFromToken(refreshToken);
        tokenRedisService.addTokenToWhitelist(refreshTokenJti, member.getId());

        return TokenDto.builder()
                .accessToken(accessToken.getTokenValue())
                .refreshToken(refreshToken.getTokenValue())
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

    public void logout(String refreshToken, HttpServletResponse response) {

        Jwt jwt = jwtTokenUtil.from(refreshToken);
        if (!tokenRedisService.validateToken(jwt)) {

            throw new IllegalArgumentException("Invalid refresh token");
        }

        String memberId = jwt.getSubject();
        tokenRedisService.addTokenToBlacklist(jwt.getId(), UUID.fromString(memberId));

        tokenResponseWriter.deleteTokens(response);
    }
}