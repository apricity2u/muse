package com.example.muse.domain.review;

import com.example.muse.domain.review.dto.*;
import com.example.muse.global.common.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("books/{bookId}/reviews")
    public ResponseEntity<ApiResponse<CreateReviewResponseDto>> createReview(
            @AuthenticationPrincipal UUID memberId,
            @PathVariable Long bookId,
            @RequestPart(value = "content") @Valid CreateReviewRequestDto createReviewRequestDto,
            @RequestPart(value = "image", required = false) MultipartFile imageFile) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse
                        .ok("리뷰 생성 성공", "CREATED",
                                reviewService.createReview(memberId, bookId, createReviewRequestDto, imageFile)
                        )
                );
    }

    @GetMapping("/reviews")
    public ResponseEntity<ApiResponse<GetReviewCardsResponseDto>> getMainReviews(
            @PageableDefault(size = 3, direction = Sort.Direction.DESC, page = 1) Pageable pageable,
            @AuthenticationPrincipal UUID memberId) {

        return ResponseEntity.ok(
                ApiResponse.ok(
                        "메인 리뷰 목록 조회 성공", "SUCCESS",
                        reviewService.getMainReviews(pageable, memberId)
                )
        );
    }

    @GetMapping("/users/{memberId}/reviews")
    public ResponseEntity<ApiResponse<GetReviewCardsResponseDto>> getUserReviews(
            @PathVariable UUID memberId,
            @PageableDefault(size = 20, direction = Sort.Direction.DESC, sort = "createdAt", page = 1) Pageable pageable,
            @AuthenticationPrincipal UUID authMemberId) {

        return ResponseEntity.ok(
                ApiResponse.ok(
                        "유저의 리뷰 목록 조회 성공", "SUCCESS",
                        reviewService.getUserReviews(pageable, memberId, authMemberId)
                )
        );
    }

    @GetMapping("/reviews/likes")
    public ResponseEntity<ApiResponse<GetLikedReviewsResponseDto>> getLikedReviews(
            @PageableDefault(size = 20, direction = Sort.Direction.DESC, sort = "createdAt") Pageable pageable,
            @AuthenticationPrincipal UUID memberId) {

        return ResponseEntity.ok()
                .body(
                        ApiResponse.ok(
                                "좋아요한 리뷰 목록 조회 성공", "SUCCESS",
                                reviewService.getLikedReviews(pageable, memberId)
                        )
                );
    }

    @PatchMapping("/reviews/{reviewId}")
    public ResponseEntity<ApiResponse<CreateReviewResponseDto>> updateReview(
            @PathVariable Long reviewId,
            @RequestPart(value = "content", required = false) @Valid UpdateReviewRequestDto requestDto,
            @RequestPart(value = "image", required = false) MultipartFile imageFile,
            @AuthenticationPrincipal UUID memberId) {

        return ResponseEntity.ok()
                .body(ApiResponse.ok("리뷰 수정에 성공했습니다.", "SUCCESS",
                                reviewService.updateReview(reviewId, requestDto, imageFile, memberId)
                        )
                );
    }


    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<ApiResponse<Void>> deleteReview(@PathVariable Long reviewId, @AuthenticationPrincipal UUID memberId) {

        reviewService.deleteReview(reviewId, memberId);
        return ResponseEntity.ok().body(
                ApiResponse.ok("리뷰를 삭제했습니다.", "SUCCESS", null)
        );
    }

    @PostMapping("/reviews/{reviewId}/like")
    public ResponseEntity<ApiResponse<Void>> reviewLike(
            @PathVariable Long reviewId,
            @AuthenticationPrincipal UUID memberId) {

        reviewService.reviewLike(reviewId, memberId);
        return ResponseEntity.ok(
                ApiResponse.ok("리뷰 좋아요 성공", "SUCCESS", null)
        );
    }

    @DeleteMapping("/reviews/{reviewId}/like")
    public ResponseEntity<ApiResponse<Void>> reviewUnlike(
            @PathVariable Long reviewId,
            @AuthenticationPrincipal UUID memberId) {

        reviewService.reviewUnlike(reviewId, memberId);
        return ResponseEntity.ok(
                ApiResponse.ok("리뷰 좋아요 취소 성공", "SUCCESS", null)
        );
    }

    @GetMapping("/books/{bookId}/reviews")
    public ResponseEntity<ApiResponse<BookReviewsResponseDto>> getBookWithReviews(
            @PathVariable Long bookId,
            @PageableDefault(size = 20, direction = Sort.Direction.DESC, sort = "createdAt")
            Pageable pageable,
            @AuthenticationPrincipal UUID memberId
    ) {
        return ResponseEntity.ok(
                ApiResponse.ok(
                        "도서의 리뷰 목록 조회 성공", "SUCCESS",
                        reviewService.getBookWithReviews(bookId, pageable, memberId)
                )
        );
    }


    @GetMapping("books/{bookId}/reviews/{reviewId}")
    public ResponseEntity<ApiResponse<GetReviewDetailResponseDto>> getReviewDetail(
            @PathVariable Long bookId,
            @PathVariable Long reviewId,
            @AuthenticationPrincipal UUID memberId
    ) {

        return ResponseEntity.ok(
                ApiResponse.ok(
                        "리뷰 조회 성공", "SUCCESS",
                        reviewService.getReview(bookId, reviewId, memberId)
                )
        );
    }

}
