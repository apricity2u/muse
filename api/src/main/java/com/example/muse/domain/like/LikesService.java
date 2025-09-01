package com.example.muse.domain.like;

import com.example.muse.domain.book.BookRepository;
import com.example.muse.domain.member.MemberRepository;
import com.example.muse.global.common.exception.CustomBadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class LikesService {
    private final LikesRepository likesRepository;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;

    public void createReviewLike(Long reviewId, UUID memberId) {

        likesRepository.upsertReviewLike(reviewId, memberId.toString());
    }

    @SneakyThrows
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void createBookLike(Long bookId, UUID memberId) {

        int maxLikeCount = 3;
        for (int i = 0; i < maxLikeCount; i++) {
            try {
                Likes likes = Likes.builder()
                        .book(bookRepository.getReferenceById(bookId))
                        .member(memberRepository.getReferenceById(memberId))
                        .build();

                likesRepository.save(likes);
                likesRepository.flush();
                break;
            } catch (Exception e) {
                Thread.sleep(100);
            }
        }
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
