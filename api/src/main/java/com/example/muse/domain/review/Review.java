package com.example.muse.domain.review;

import com.example.muse.domain.book.Book;
import com.example.muse.domain.image.Image;
import com.example.muse.domain.like.Likes;
import com.example.muse.domain.member.Member;
import com.example.muse.domain.review.dto.UpdateReviewRequestDto;
import com.example.muse.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Review extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "image_id")
    private Image image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @BatchSize(size = 10)
    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Likes> likes = new ArrayList<>();

    public Review update(UpdateReviewRequestDto requestDto, Image image) {

        if (requestDto != null && requestDto.getContent() != null) {
            this.content = requestDto.getContent();
        }

        if (image != null) {
            this.image = image;
        }

        return this;
    }
}
