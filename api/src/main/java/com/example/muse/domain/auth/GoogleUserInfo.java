package com.example.muse.domain.auth;

import com.example.muse.domain.member.Provider;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component("google")
public class GoogleUserInfo implements OAuth2UserInfo {
    @Override
    public Provider getProvider() {
        return Provider.GOOGLE;
    }

    @Override
    public String getProviderKey(OAuth2User user) {
        return user.getAttribute("sub");
    }

    @Override
    public String getNickname(OAuth2User user) {
        return user.getAttribute("name");
    }
}
