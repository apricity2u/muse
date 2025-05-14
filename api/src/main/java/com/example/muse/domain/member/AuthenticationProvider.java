package com.example.muse.domain.member;

import jakarta.persistence.*;
        import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthenticationProvider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private Provider provider;
    private String providerKey;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Builder
    public AuthenticationProvider(Provider provider, String providerKey, Member member) {
        this.provider = provider;
        this.providerKey = providerKey;
        this.member = member;
    }

}
