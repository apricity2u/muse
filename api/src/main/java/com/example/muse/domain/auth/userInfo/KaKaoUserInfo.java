package com.example.muse.domain.auth.userInfo;

import com.example.muse.domain.member.Provider;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component("kakao")
public class KaKaoUserInfo implements OAuth2UserInfo {

    @Override
    public Provider getProvider() {
        return Provider.KAKAO;
    }

    @Override
    public String getProviderKey(OAuth2User user) {
        return user.getAttribute("sub");
    }

    @Override
    public String getNickname(OAuth2User user) {

        return user.getAttribute("nickname");
    }
}
