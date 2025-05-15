package com.example.muse.domain.auth;

import com.example.muse.domain.member.Provider;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;

@Component("kakao")
public class KaKaoUserInfo implements OAuth2UserInfo {

    @Override
    public Provider getProvider() {
        return Provider.KAKAO;
    }

    @Override
    public String getProviderKey(OidcUser user) {
        return user.getAttribute("sub");
    }

    @Override
    public String getNickname(OidcUser user) {

        return user.getAttribute("nickname");
    }
}
