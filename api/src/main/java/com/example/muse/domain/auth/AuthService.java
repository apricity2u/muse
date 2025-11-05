package com.example.muse.domain.auth;

import com.example.muse.domain.auth.dto.LoginResponseDto;
import com.example.muse.domain.auth.dto.TokenDto;
import com.example.muse.domain.auth.userInfo.OAuth2UserInfo;
import com.example.muse.domain.image.ImageRepository;
import com.example.muse.domain.member.*;
import com.example.muse.domain.member.dto.GetProfileResponseDto;
import com.example.muse.global.common.exception.CustomLoginException;
import com.example.muse.global.common.exception.CustomOauthException;
import com.example.muse.global.common.exception.CustomReissueException;
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
@Transactional()
public class AuthService {
    private final MemberRepository memberRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final List<OAuth2UserInfo> userInfoStrategies;
    private final TokenRedisService tokenRedisService;
    private final TokenResponseWriter tokenResponseWriter;
    private final ImageRepository imageRepository;
    private final MemberService memberService;


    public Member processLogin(Authentication authentication) {

        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        OAuth2User oauth2User = oauthToken.getPrincipal();

        Provider provider = Provider.valueOf(
                oauthToken.getAuthorizedClientRegistrationId().toUpperCase()
        );
        OAuth2UserInfo userInfo = userInfoStrategies.stream()
                .filter(strategy -> strategy.getProvider() == provider)
                .findFirst()
                .orElseThrow(CustomOauthException::new);

        String providerKey = userInfo.getProviderKey(oauth2User);
        String nickname = userInfo.getNickname(oauth2User);

        Optional<Member> optionalMember = memberRepository
                .findByAuthenticationProvidersProviderAndAuthenticationProvidersProviderKey(provider, providerKey);


        return optionalMember.orElseGet(
                () -> signup(provider, providerKey, nickname)
        );
    }


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

        Jwt jwt = jwtTokenUtil.tokenFrom(refreshToken);
        if (!tokenRedisService.validateToken(jwt)) {

            throw new CustomLoginException();
        }

        String jti = jwt.getId();
        String memberId = jwt.getSubject();
        tokenRedisService.addTokenToBlacklist(jti, UUID.fromString(memberId));
        tokenRedisService.deleteTokenFromWhitelist(jti);

        tokenResponseWriter.deleteTokens(response);
    }

    public LoginResponseDto reissue(String refreshToken, HttpServletResponse response) {

        Jwt jwt = jwtTokenUtil.tokenFrom(refreshToken);
        if (!tokenRedisService.validateToken(jwt)) {

            throw new CustomReissueException();
        }

        UUID memberId = UUID.fromString(jwt.getSubject());
        GetProfileResponseDto profile = memberService.getProfile(memberId);
        Member member = profile.toMember();

        Jwt accessToken = jwtTokenUtil.createAccessToken(member);
        Jwt newRefreshToken = jwtTokenUtil.createRefreshToken(member);
        String jti = jwtTokenUtil.getJtiFromToken(newRefreshToken);

        logout(refreshToken, response);
        tokenRedisService.addTokenToWhitelist(jti, memberId);
        tokenResponseWriter.writeTokens(response, accessToken, newRefreshToken);


        return LoginResponseDto.from(profile);
    }

    @Transactional(readOnly = true)
    public LoginResponseDto getLoginWithData(UUID memberId) {

        Member member = memberId == null ? null : memberRepository.getReferenceById(memberId);
        String profileImageUrl = imageRepository.findProfileImageUrlByMemberId(memberId)
                .orElse(null);

        return LoginResponseDto.from(member, profileImageUrl);
    }
}