package com.example.muse.domain.auth;

import com.example.muse.domain.member.Provider;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

public interface OAuth2UserInfo {
    Provider getProvider();
    String getProviderKey(OidcUser user);
    String getNickname(OidcUser user);
}
