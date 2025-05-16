package com.example.muse.domain.auth;

import com.example.muse.domain.member.Provider;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component("naver")
public class NaverUserInfo implements OAuth2UserInfo {
    @Override
    public Provider getProvider() {
        return Provider.NAVER;
    }

    @Override
    public String getProviderKey(OAuth2User user) {
        HashMap<String, String> props = user.getAttribute("response");

        return props.get("id");
    }

    @Override
    public String getNickname(OAuth2User user) {
        HashMap<String, String> props = user.getAttribute("response");

        return props.get("nickname");
    }
}
