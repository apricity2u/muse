package com.example.muse.domain.member;

import com.example.muse.domain.image.Image;
import com.example.muse.domain.image.ImageType;
import com.example.muse.domain.like.Likes;
import com.example.muse.domain.review.Review;
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
    private Image profileImage;

    @OneToMany(mappedBy = "member", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Likes> likes = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Image> images = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Review> reviews = new ArrayList<>();

    @Transient
    private Map<String, Object> attributes;

    @Builder.Default
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

    @PostLoad
    private void initializeProfileImage() {
        this.profileImage = images.stream()
                .filter(image -> image.getImageType() == ImageType.PROFILE)
                .findAny()
                .orElse(null);
    }

    public void addAuthenticationProviders(AuthenticationProvider authenticationProvider) {
        authenticationProviders.add(authenticationProvider);
        authenticationProvider.setMember(this);
    }

    public Member update(String nickname, Image image) {
        if (nickname != null) {
            this.nickname = nickname;
        }
        if (image != null) {
            this.images.add(image);
            this.profileImage = image;
        }

        return this;
    }

    public String getProfileImageUrl() {

        return profileImage == null ? null : profileImage.getImageUrl();
    }
}
