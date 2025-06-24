package com.example.muse.domain.book.dto;

import com.example.muse.domain.book.Book;
import com.example.muse.domain.like.Likes;
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

        Likes likes = Likes.builder()
                .member(member)
                .book(book)
                .build();

        boolean isLiked = member != null &&
                book.getLikes().contains(likes);

        return BookDto.builder()
                .id(book.getId())
                .imageUrl(book.getImageUrl())
                .title(book.getTitle())
                .author(book.getAuthor().replace("^", " "))
                .publisher(book.getPublisher())
                .likeCount(book.getLikes().size())
                .isLike(isLiked)
                .publishedDate(book.getPublishedDate())
                .isbn(book.getIsbn())
                .build();
    }

    public static BookDto from(Book book, Member member, boolean isLiked) {

        return BookDto.builder()
                .id(book.getId())
                .imageUrl(book.getImageUrl())
                .title(book.getTitle())
                .author(book.getAuthor().replace("^", " "))
                .publisher(book.getPublisher())
                .likeCount(book.getLikes().size())
                .isLike(isLiked)
                .publishedDate(book.getPublishedDate())
                .isbn(book.getIsbn())
                .build();
    }
}