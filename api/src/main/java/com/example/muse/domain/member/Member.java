package com.example.muse.domain.member;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member implements OAuth2User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "CHAR(36)")
    private UUID id;

    @Column(nullable = false)
    private String nickname;

    @Transient
    private Map<String, Object> attributes;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<AuthenticationProvider> authenticationProviders = new ArrayList<>();

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getName() {
        return nickname;
    }

    @Builder
    public Member(String nickname, Map<String, Object> attributes, List<AuthenticationProvider> authenticationProviders) {
        this.nickname = nickname;
        this.attributes = attributes;
        this.authenticationProviders = authenticationProviders;
    }
}
