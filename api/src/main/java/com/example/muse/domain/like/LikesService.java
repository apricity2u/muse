package com.example.muse.domain.like;

import com.example.muse.domain.outbox.OutBoxEvent;
import com.example.muse.domain.outbox.OutBoxEventRepository;
import com.example.muse.domain.review.Review;
import com.example.muse.global.common.exception.CustomBadRequestException;
import com.example.muse.global.messaging.config.RabbitConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class LikesService {
    private final LikesRepository likesRepository;
    private final OutBoxEventRepository outBoxEventRepository;
    private final ObjectMapper objectMapper;

    public void createReviewLike(Long reviewId, UUID actorId) {

        Review review = likesRepository.upsertReviewLike(reviewId, actorId.toString());
        Map<String, Object> payload = Map.of(
                "eventId", UUID.randomUUID(),
                "type", RabbitConfig.ROUTING_KEY_LIKE,
                "actorId", actorId,
                "receiverId", review.getMember().getId(),
                "reviewId", reviewId,
                "action", "LIKE"
        );

        try {
            String payloadJson = objectMapper.writeValueAsString(payload);
            OutBoxEvent event = OutBoxEvent.builder()
                    .eventId((UUID) payload.get("eventId"))
                    .type(RabbitConfig.ROUTING_KEY_LIKE)
                    .payload(payloadJson)
                    .build();

            outBoxEventRepository.save(event);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
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
