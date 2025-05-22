package com.example.muse.domain.book.dto;

import com.example.muse.domain.book.Book;
import jakarta.validation.constraints.Null;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class BookDto {
    private Long id;
    private String imageUrl;
    private String title;
    private String author;
    private String publisher;
    private long likeCount;
    private boolean isLiked;

    public static BookDto from(Book book, @Null UUID memberId) {
        boolean isLiked = memberId != null &&
                book.getLikes().stream()
                        .anyMatch(like -> like.getMember().getId().equals(memberId));

        return BookDto.builder()
                .id(book.getId())
                .imageUrl(book.getImageUrl())
                .title(book.getTitle())
                .author(book.getAuthor())
                .publisher(book.getPublisher())
                .likeCount(book.getLikes().size())
                .isLiked(isLiked)
                .build();
    }
}