package com.example.muse.domain.book.dto;

import com.example.muse.domain.book.Book;
import com.example.muse.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private long likeCount;
    private boolean isLike;

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
                .build();
    }
}