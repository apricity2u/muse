package com.example.muse.domain.book.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetBooksResponseDto {
    private List<BookDto> books;
    private long page;
    private long totalPages;
    private boolean hasNext;
    private boolean hasPrevious;
    private long totalElements;

    public static GetBooksResponseDto from(Page<BookDto> books) {

        return GetBooksResponseDto.builder()
                .books(books.getContent())
                .page(books.getNumber() + 1)
                .totalPages(books.getTotalPages())
                .hasNext(books.hasNext())
                .hasPrevious(books.hasPrevious())
                .totalElements(books.getTotalElements())
                .build();
    }
}
