package com.example.muse.domain.review;

import com.example.muse.domain.member.Member;
import com.example.muse.domain.review.dto.CreateReviewRequestDto;
import com.example.muse.domain.review.dto.CreateReviewResponseDto;
import com.example.muse.domain.review.dto.GetMainReviewsResponseDto;
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
                        ));
    }

    @GetMapping("reviews")
    public ResponseEntity<ApiResponse<GetMainReviewsResponseDto>> getMainReviews(@PageableDefault(size = 3, direction = Sort.Direction.DESC) Pageable pageable,
                                                                                 @AuthenticationPrincipal Member member) {

        return ResponseEntity.ok(
                ApiResponse.ok(
                        reviewService.getMainReviews(pageable, member)
                )
        );
    }
}
