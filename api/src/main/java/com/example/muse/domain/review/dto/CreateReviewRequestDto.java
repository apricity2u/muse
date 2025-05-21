package com.example.muse.domain.review.dto;

import com.example.muse.domain.book.Book;
import com.example.muse.domain.image.Image;
import com.example.muse.domain.member.Member;
import com.example.muse.domain.review.Review;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateReviewRequestDto {
    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    public Review toEntity(Image image, Member member, Book book) {

        return Review.builder()
                .content(content)
                .image(image)
                .member(member)
                .book(book)
                .build();
    }
}
