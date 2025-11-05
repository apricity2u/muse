package com.example.muse.domain.book.dto;

import com.example.muse.domain.book.Book;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SearchBookResponseDto {
    private long id;
    private String title;

    public static SearchBookResponseDto from(Book book) {

        return new SearchBookResponseDto(book.getId(), book.getTitle());
    }
}
