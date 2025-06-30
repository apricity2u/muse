package com.example.muse.domain.book.dto;

import com.example.muse.domain.book.Book;
import com.example.muse.domain.like.Likes;
import com.example.muse.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetBookResponseDto {
    private Long bookId;
    private String imageUrl;
    private String title;
    private String author;
    private String publisher;
    private LocalDate publishedDate;
    private String isbn;
    private String description;
    private boolean isLike;
    private List<String> descriptionParagraphs;

    public static GetBookResponseDto from(Book book, Member member) {

        Likes like = Likes.builder()
                .member(member)
                .book(book)
                .build();

        boolean isLike = member != null && book.getLikes().contains(like);

        return GetBookResponseDto.builder()
                .bookId(book.getId())
                .imageUrl(book.getImageUrl())
                .title(book.getTitle())
                .author(book.getAuthor().replace("^", " "))
                .publisher(book.getPublisher())
                .publishedDate(book.getPublishedDate())
                .isbn(book.getIsbn())
                .description(book.getDescription())
                .descriptionParagraphs(List.of(book.getDescription().split("\n")))
                .isLike(isLike)
                .build();
    }
}