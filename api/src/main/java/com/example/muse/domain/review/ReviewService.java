package com.example.muse.domain.review;

import com.example.muse.domain.book.Book;
import com.example.muse.domain.book.BookService;
import com.example.muse.domain.image.Image;
import com.example.muse.domain.image.ImageService;
import com.example.muse.domain.image.ImageType;
import com.example.muse.domain.member.Member;
import com.example.muse.domain.review.dto.CreateReviewRequestDto;
import com.example.muse.domain.review.dto.CreateReviewResponseDto;
import com.example.muse.domain.review.dto.GetMainReviewsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final BookService bookService;
    private final ImageService imageService;


    @Transactional
    public CreateReviewResponseDto createReview(Member member, Long bookId, CreateReviewRequestDto createReviewRequestDto, MultipartFile imageFile) {

        Book book = bookService.findById(bookId);
        // TODO: 기본이미지 결정 후 변경
        Image image = imageService.uploadImage(imageFile, ImageType.REVIEW);
        Review review = createReviewRequestDto.toEntity(image, member, book);
        review = reviewRepository.save(review);

        return CreateReviewResponseDto.from(review);
    }

    public GetMainReviewsResponseDto getMainReviews(Pageable pageable, Member member) {

        Page<Review> reviews = reviewRepository.findMainReviews(pageable);

        return GetMainReviewsResponseDto.from(reviews, member);
    }
}
