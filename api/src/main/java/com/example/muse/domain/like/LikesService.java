package com.example.muse.domain.like;

import com.example.muse.global.common.exception.CustomBadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class LikesService {
    private final LikesRepository likesRepository;

    public void createReviewLike(Long reviewId, UUID memberId) {

        likesRepository.upsertReviewLike(reviewId, memberId.toString());
    }

    public void createBookLike(Long bookId, UUID memberId) {

        likesRepository.upsertBookLike(bookId, memberId.toString());
    }


    public void unLikeReview(Long reviewId, UUID memberId) {

        int deleteCount = likesRepository.deleteByReviewIdAndMember(reviewId, memberId);
        if (deleteCount == 0) {
            throw new CustomBadRequestException("좋아요가 존재하지 않습니다.");
        }
    }

    public void unLikeBook(Long bookId, UUID memberId) {

        int deleteCount = likesRepository.deleteByBookIdAndMember(bookId, memberId);
        if (deleteCount == 0) {
            throw new CustomBadRequestException("좋아요가 존재하지 않습니다.");
        }
    }
}
