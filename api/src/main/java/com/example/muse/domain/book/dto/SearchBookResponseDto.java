package com.example.muse.domain.book.dto;

import com.example.muse.domain.book.Book;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SearchBookResponseDto {
    private List<SimpleBookDto> data;

    public static SearchBookResponseDto from(List<Book> bookList) {

        List<SimpleBookDto> simpleBookDtoList = bookList.stream()
                .map(SimpleBookDto::from)
                .toList();

        return new SearchBookResponseDto(simpleBookDtoList);
    }


    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SimpleBookDto {
        private long id;
        private String title;

        public static SimpleBookDto from(Book book) {
            return new SimpleBookDto(book.getId(), book.getTitle());
        }
    }
}
