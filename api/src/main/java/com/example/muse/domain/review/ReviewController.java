package com.example.muse.domain.review;

import com.example.muse.domain.member.Member;
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
            @AuthenticationPrincipal Member member,
            @PathVariable Long bookId,
            @RequestPart(value = "content") @Valid CreateReviewRequestDto createReviewRequestDto,
            @RequestPart(value = "image", required = false) MultipartFile imageFile) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse
                        .ok("리뷰 생성 성공", "CREATED",
                                reviewService.createReview(member, bookId, createReviewRequestDto, imageFile)
                        )
                );
    }

    @GetMapping("/reviews")
    public ResponseEntity<ApiResponse<GetReviewsResponseDto>> getMainReviews(@PageableDefault(size = 3, direction = Sort.Direction.DESC) Pageable pageable,
                                                                             @AuthenticationPrincipal Member member) {

        return ResponseEntity.ok(
                ApiResponse.ok(
                        reviewService.getMainReviews(pageable, member)
                )
        );
    }

    @GetMapping("/users/{memberId}/reviews")
    public ResponseEntity<ApiResponse<GetReviewsResponseDto>> getUserReviews(
            @PathVariable UUID memberId,
            @PageableDefault(size = 20, direction = Sort.Direction.DESC, sort = "createdAt") Pageable pageable,
            @AuthenticationPrincipal Member loggedInMember) {

        return ResponseEntity.ok(
                ApiResponse.ok(
                        reviewService.getUserReviews(pageable, memberId, loggedInMember)
                )
        );
    }

    @PatchMapping("/reviews/{reviewId}")
    public ResponseEntity<ApiResponse<CreateReviewResponseDto>> updateReview(
            @PathVariable Long reviewId,
            @RequestPart(value = "content", required = false) @Valid UpdateReviewRequestDto requestDto,
            @RequestPart(value = "image", required = false) MultipartFile imageFile,
            @AuthenticationPrincipal Member member) {

        return ResponseEntity.ok()
                .body(ApiResponse.ok("리뷰 수정에 성공했습니다.", "SUCCESS",
                                reviewService.updateReview(reviewId, requestDto, imageFile, member)
                        )
                );
    }


    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<ApiResponse<Void>> deleteReview(@PathVariable Long reviewId, @AuthenticationPrincipal Member member) {

        reviewService.deleteReview(reviewId, member);
        return ResponseEntity.ok().body(
                ApiResponse.ok("리뷰를 삭제했습니다.", "SUCCESS", null)
        );
    }

    @GetMapping("/reviews/likes")
    public ResponseEntity<ApiResponse<GetLikedReviewsResponseDto>> getLikedReviews(
            @PageableDefault(size = 20, direction = Sort.Direction.DESC, sort = "createdAt") Pageable pageable,
            @AuthenticationPrincipal Member member) {

        return ResponseEntity.ok()
                .body(
                        ApiResponse.ok(
                                "좋아요한 리뷰 목록 조회 성공", "SUCCESS",
                                reviewService.getLikedReviews(pageable, member)
                        )
                );
    }
}
