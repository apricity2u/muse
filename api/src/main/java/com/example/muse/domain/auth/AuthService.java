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

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AuthService {
    private final MemberRepository memberRepository;
    private final JwtTokenUtil jwtTokenUtil;


    /**
     * OAuth2 인증 정보를 기반으로 사용자를 로그인 처리하고 JWT 토큰을 반환합니다.
     *
     * 인증 객체에서 OIDC 사용자 정보를 추출하여 기존 회원 여부를 확인한 뒤,
     * 기존 회원이면 로그인 토큰을 발급하고, 없으면 회원가입 후 토큰을 발급합니다.
     *
     * @param authentication OAuth2 인증 정보가 포함된 Authentication 객체
     * @return JWT 액세스 토큰과 리프레시 토큰이 포함된 TokenDto
     */
    public TokenDto processLogin(Authentication authentication) {
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
        } else {

            member = signup(provider, providerKey, nickname);
        }

        return login(member);

    }

    /**
     * 주어진 회원 정보를 기반으로 JWT 액세스 토큰과 리프레시 토큰을 생성하여 반환합니다.
     *
     * @param member 인증된 회원 정보
     * @return 생성된 액세스 토큰과 리프레시 토큰이 포함된 TokenDto
     */
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

    /**
     * 주어진 OAuth2 제공자 정보와 닉네임으로 새로운 회원을 생성하고 저장합니다.
     *
     * @param provider OAuth2 인증 제공자
     * @param sub 제공자별 사용자 고유 식별자
     * @param nickname 회원 닉네임
     * @return 생성 및 저장된 회원 엔티티
     */
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