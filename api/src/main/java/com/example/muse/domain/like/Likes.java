package com.example.muse.domain.like;

import com.example.muse.domain.book.Book;
import com.example.muse.domain.member.Member;
import com.example.muse.domain.review.Review;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"member_id", "book_id"}),
                @UniqueConstraint(columnNames = {"member_id", "review_id"})
        })
public class Likes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Likes)) return false;
        Likes other = (Likes) o;
        UUID memberId = member != null ? member.getId() : null;
        UUID otherMemberId = other.member != null ? other.member.getId() : null;
        if (!Objects.equals(memberId, otherMemberId)) return false;

        if (this.review != null || other.review != null) {
            Long reviewId = this.review != null ? this.review.getId() : null;
            Long otherReviewId = other.review != null ? other.review.getId() : null;
            return Objects.equals(reviewId, otherReviewId);
        }

        Long bookId = book != null ? book.getId() : null;
        Long otherBookId = other.book != null ? other.book.getId() : null;
        return Objects.equals(bookId, otherBookId);
    }

    @Override
    public int hashCode() {
        UUID memberId = member != null ? member.getId() : null;
        if (review != null) {
            return Objects.hash(memberId, review.getId());
        }
        return Objects.hash(memberId, book != null ? book.getId() : null);
    }
}
