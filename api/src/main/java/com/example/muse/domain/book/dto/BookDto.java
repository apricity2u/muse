package com.example.muse.domain.book.dto;

import com.example.muse.domain.book.Book;
import com.example.muse.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {
    private Long id;
    private String imageUrl;
    private String title;
    private String author;
    private String publisher;
    private LocalDate publishedDate;
    private long likeCount;
    private boolean isLike;
    private String isbn;

    public static BookDto from(Book book, Member member) {
        boolean isLiked = member != null &&
                book.getLikes().stream()
                        .anyMatch(like -> like.getMember().getId().equals(member.getId()));

        return BookDto.builder()
                .id(book.getId())
                .imageUrl(book.getImageUrl())
                .title(book.getTitle())
                .author(book.getAuthor())
                .publisher(book.getPublisher())
                .likeCount(book.getLikes().size())
                .isLike(isLiked)
                .publishedDate(book.getPublishedDate())
                .isbn(book.getIsbn())
                .build();
    }
}