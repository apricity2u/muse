package com.example.muse.domain.image;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ImageRepository extends JpaRepository<Image, Long> {

    @EntityGraph(attributePaths = {"member"})
    @Query("SELECT i FROM Image i WHERE i.member.id = :memberId AND i.imageType = 'PROFILE'")
    Optional<Image> findProfileImageByMemberId(@Param("memberId") UUID memberId);

    @EntityGraph(attributePaths = {"member"})
    List<Image> findAllByMemberIdInAndImageType(List<UUID> memberIds, ImageType imageType);
}
