package com.example.muse.domain.member;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MemberRepository extends JpaRepository<Member, UUID> {

    Optional<Member> findByAuthenticationProvidersProviderAndAuthenticationProvidersProviderKey(
            Provider provider,
            String providerKey
    );
    
    Optional<Member> findById(UUID memberId);
}
