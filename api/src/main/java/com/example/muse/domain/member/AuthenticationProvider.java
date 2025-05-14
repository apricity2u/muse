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

    /**
     * 지정한 인증 제공자, 제공자 키, 회원 정보를 사용하여 AuthenticationProvider 엔티티를 생성합니다.
     *
     * @param provider 인증 제공자 유형
     * @param providerKey 인증 제공자에 연결된 고유 키
     * @param member 연관된 회원 엔티티
     */
    @Builder
    public AuthenticationProvider(Provider provider, String providerKey, Member member) {
        this.provider = provider;
        this.providerKey = providerKey;
        this.member = member;
    }

}
