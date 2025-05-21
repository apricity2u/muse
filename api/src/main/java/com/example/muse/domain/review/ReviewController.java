package com.example.muse.domain.review;

import com.example.muse.domain.member.Member;
import com.example.muse.domain.review.dto.CreateReviewRequestDto;
import com.example.muse.global.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
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
    public void createReview(
            @AuthenticationPrincipal Member member,
            @PathVariable Long bookId,
            @RequestPart(value = "content") CreateReviewRequestDto createReviewRequestDto,
            @RequestPart(value = "image", required = false) MultipartFile imageFile) {

        ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse
                        .ok("리뷰 생성 성공", "CREATED",
                                reviewService.createReview(member, bookId, createReviewRequestDto, imageFile)
                        ));
    }
}
