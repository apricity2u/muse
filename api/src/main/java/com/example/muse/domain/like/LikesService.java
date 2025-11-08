package com.example.muse.domain.like;

import com.example.muse.domain.book.BookRepository;
import com.example.muse.domain.member.MemberRepository;
import com.example.muse.domain.outbox.OutBoxEvent;
import com.example.muse.domain.outbox.OutBoxEventRepository;
import com.example.muse.domain.review.Review;
import com.example.muse.domain.review.ReviewRepository;
import com.example.muse.global.common.exception.CustomBadRequestException;
import com.example.muse.global.common.exception.CustomNotFoundException;
import com.example.muse.global.lock.RedissonLockTemplate;
import com.example.muse.global.messaging.config.RabbitConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class LikesService {
    private final LikesRepository likesRepository;
    private final OutBoxEventRepository outBoxEventRepository;
    private final ObjectMapper objectMapper;
    private final ReviewRepository reviewRepository;
    private final RedissonLockTemplate redissonLockTemplate;
    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;

    private String reviewLikeLockKey(Long reviewId, UUID memberId) {

        return "lock:like:review:" + reviewId + ":member:" + memberId;
    }

    private String bookLikeLockKey(Long bookId, UUID memberId) {

        return "lock:like:book:" + bookId + ":member:" + memberId;
    }

    public void createBookLike(Long bookId, UUID actorId) {

        redissonLockTemplate.executeWithLock(bookLikeLockKey(bookId, actorId), () -> {
            boolean exists = likesRepository.existsByBookIdAndMemberId(bookId, actorId);
            if (exists) {
                throw new CustomBadRequestException("이미 좋아요를 누른 상태입니다.");
            }
            Likes like = Likes.builder()
                    .book(bookRepository.getReferenceById(bookId))
                    .member(memberRepository.getReferenceById(actorId))
                    .build();
            likesRepository.save(like);
            return null;
        });
    }

    @SneakyThrows
    public void createReviewLike(Long reviewId, UUID actorId) {

        redissonLockTemplate.executeWithLock(reviewLikeLockKey(reviewId, actorId), () -> {
            boolean exists = likesRepository.existsByReviewIdAndMemberId(reviewId, actorId);
            if (exists) {
                throw new CustomBadRequestException("이미 좋아요를 누른 상태입니다.");
            }
            Likes like = Likes.builder()
                    .review(reviewRepository.getReferenceById(reviewId))
                    .member(memberRepository.getReferenceById(actorId))
                    .build();
            likesRepository.save(like);
            return null;
        });

        Review review = reviewRepository.findById(reviewId).orElseThrow(CustomNotFoundException::new);
        UUID eventId = UUID.randomUUID();
        Map<String, Object> payload = Map.of(
                "eventId", eventId,
                "type", RabbitConfig.ROUTING_KEY_LIKE,
                "actorId", actorId,
                "receiverId", review.getMember().getId(),
                "reviewId", reviewId,
                "action", "LIKE"
        );


        String payloadJson = objectMapper.writeValueAsString(payload);
        OutBoxEvent event = OutBoxEvent.builder()
                .eventId(eventId)
                .type(RabbitConfig.ROUTING_KEY_LIKE)
                .payload(payloadJson)
                .build();

        outBoxEventRepository.save(event);
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
