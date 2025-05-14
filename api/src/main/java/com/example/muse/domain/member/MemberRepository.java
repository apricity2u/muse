package com.example.muse.domain.member;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    /**
     * 지정된 인증 제공자와 제공자 키에 해당하는 회원을 조회합니다.
     *
     * @param provider 회원의 인증 제공자 유형
     * @param providerKey 인증 제공자에서 발급한 고유 키
     * @return 조건에 일치하는 회원이 존재하면 Optional<Member>를 반환하며, 없으면 빈 Optional을 반환합니다.
     */
    Optional<Member> findByAuthenticationProvidersProviderAndAuthenticationProvidersProviderKey(
            Provider provider,
            String providerKey
    );
}
