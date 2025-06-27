package com.example.muse.domain.review.dto;

import com.example.muse.domain.book.dto.BookDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookReviewsResponseDto {
    private BookDto book;
    private List<ReviewWithUserDto> reviews;
    private long page;
    private long totalPages;
    private boolean hasNext;
    private boolean hasPrevious;
    private long totalElements;
}
