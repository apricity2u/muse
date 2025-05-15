package com.example.muse.domain.auth;

import com.example.muse.domain.member.Provider;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface OAuth2UserInfo {
    Provider getProvider();
    String getProviderKey(OAuth2User user);
    String getNickname(OAuth2User user);
}
