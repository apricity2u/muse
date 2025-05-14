package com.example.muse.domain.member;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.sql.Types;
import java.util.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Member implements OAuth2User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(Types.VARCHAR)
    @Column(columnDefinition = "CHAR(36)")
    private UUID id;

    @Column(nullable = false)
    private String nickname;

    @Transient
    private Map<String, Object> attributes;

    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<AuthenticationProvider> authenticationProviders = new ArrayList<>();

    /**
     * OAuth2 사용자 속성 맵을 반환합니다.
     *
     * @return OAuth2 인증 시 제공된 사용자 속성의 맵
     */
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    /**
     * 이 멤버에 대한 권한 정보를 반환합니다.
     *
     * 항상 빈 권한 목록을 반환합니다.
     *
     * @return 빈 권한 목록
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    /**
     * OAuth2 사용자 이름으로 회원의 닉네임을 반환합니다.
     *
     * @return 회원의 닉네임
     */
    @Override
    public String getName() {
        return nickname;
    }



    /**
     * 회원의 인증 제공자 목록에 새로운 인증 제공자를 추가하고, 해당 제공자의 소유자를 이 회원으로 설정합니다.
     *
     * @param authenticationProvider 추가할 인증 제공자 객체
     */
    public void addAuthenticationProviders(AuthenticationProvider authenticationProvider) {
        authenticationProviders.add(authenticationProvider);
        authenticationProvider.setMember(this);
    }
}
