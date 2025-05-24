package com.example.muse.domain.like;

import com.example.muse.domain.book.Book;
import com.example.muse.domain.member.Member;
import com.example.muse.domain.review.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class LikesService {
    private final LikesRepository likesRepository;

    public void createLike(Book book, Member member) {

        Likes likes = Likes.builder()
                .book(book)
                .member(member)
                .build();

        likesRepository.save(likes);
    }

    public void createLike(Review review, Member member) {

        Likes likes = Likes.builder()
                .review(review)
                .member(member)
                .build();

        likesRepository.save(likes);
    }

    public void unLike(Long reviewId, Member member) {

        int isDelete = likesRepository.deleteByReviewIdAndMember(reviewId, member);
        if (isDelete == 0) {
            throw new IllegalArgumentException("좋아요가 존재하지 않습니다.");
        }

    }
}
