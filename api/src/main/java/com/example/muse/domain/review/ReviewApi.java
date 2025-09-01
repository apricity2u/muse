package com.example.muse.domain.review;

import com.example.muse.domain.review.dto.CreateReviewRequestDto;
import com.example.muse.domain.review.dto.CreateReviewResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Tag(name = "Review", description = "리뷰 관련 API")
public interface ReviewApi {
    @Operation(summary = "리뷰 생성", description = "도서에 대한 리뷰를 생성합니다.")
    @ApiResponse(responseCode = "201",
            description = "리뷰 생성 성공",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CreateReviewResponseDto.class)))
    ResponseEntity<com.example.muse.global.common.dto.ApiResponse<CreateReviewResponseDto>> createReview(
            @Parameter(hidden = true) UUID memberId,
            @Parameter(description = "도서 ID", example = "1") @PathVariable Long bookId,
            @Parameter(description = "리뷰 내용 (multipart의 content)") CreateReviewRequestDto createReviewRequestDto,
            @Parameter(description = "리뷰 이미지", required = false) MultipartFile imageFile);
}