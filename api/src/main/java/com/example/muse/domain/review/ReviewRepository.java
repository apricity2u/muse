package com.example.muse.domain.review;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("""
                        SELECT r FROM Review r
                        LEFT JOIN r.likes l
                        GROUP BY r
                        ORDER BY COUNT(l) DESC
            """)
    Page<Review> findMainReviews(Pageable pageable);

    // TODO
    Page<Review> findByMemberId(Pageable pageable, UUID memberId);
}
