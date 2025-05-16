package com.example.muse.domain.member;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByAuthenticationProvidersProviderAndAuthenticationProvidersProviderKey(
            Provider provider,
            String providerKey
    );
}
