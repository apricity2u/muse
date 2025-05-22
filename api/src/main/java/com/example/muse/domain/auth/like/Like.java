package com.example.muse.domain.auth.like;

import com.example.muse.domain.book.Book;
import com.example.muse.domain.member.Member;
import com.example.muse.domain.review.Review;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Like {
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
}
