package com.example.muse.domain.review.dto;

import com.example.muse.domain.book.dto.BookDto;
import com.example.muse.domain.image.dto.ImageDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDetailDto {
    private Long reviewId;
    private String content;
    private String createdAt;
    private String updatedAt;
    private ImageDto image;
    private BookDto book;
}
