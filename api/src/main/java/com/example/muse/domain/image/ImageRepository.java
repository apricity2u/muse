package com.example.muse.domain.image;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {

    @Query(value = """
            SELECT * FROM image
                                 WHERE member_id = :memberId
                                   AND image_type = 'PROFILE'
                                 ORDER BY created_at DESC
                                 LIMIT 1""", nativeQuery = true)
    Optional<Image> findLastProfileImageByMemberId(@Param("memberId") String memberId);
}
