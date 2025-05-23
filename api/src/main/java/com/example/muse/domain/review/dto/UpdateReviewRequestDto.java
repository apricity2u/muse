package com.example.muse.domain.review.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateReviewRequestDto {
    @NotBlank(message = "내용을 입력해주세요.")
    private String content;
}
