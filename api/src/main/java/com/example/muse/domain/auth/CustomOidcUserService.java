package com.example.muse.domain.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CustomOidcUserService extends OidcUserService {
    /**
     * OIDC 사용자 요청을 처리하여 사용자 정보를 반환합니다.
     *
     * @param userRequest OIDC 사용자 요청 정보
     * @return 인증된 OIDC 사용자 정보
     * @throws OAuth2AuthenticationException 인증 과정에서 오류가 발생한 경우
     */
    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {

        return super.loadUser(userRequest);
    }
}
