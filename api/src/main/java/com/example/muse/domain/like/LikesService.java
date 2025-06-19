package com.example.muse.domain.like;

import com.example.muse.domain.book.Book;
import com.example.muse.domain.member.Member;
import com.example.muse.domain.review.Review;
import com.example.muse.global.common.exception.CustomBadRequestException;
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

    public void unLikeReview(Long reviewId, Member member) {

        int deleteCount = likesRepository.deleteByReviewIdAndMember(reviewId, member);
        if (deleteCount == 0) {
            throw new CustomBadRequestException("좋아요가 존재하지 않습니다.");
        }
    }

    public void unLikeBook(Long bookId, Member member) {

        int deleteCount = likesRepository.deleteByBookIdAndMember(bookId, member);
        if (deleteCount == 0) {
            throw new CustomBadRequestException("좋아요가 존재하지 않습니다.");
        }
    }
}
