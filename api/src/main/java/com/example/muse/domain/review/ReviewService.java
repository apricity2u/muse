package com.example.muse.domain.review;

import com.example.muse.domain.book.Book;
import com.example.muse.domain.book.BookService;
import com.example.muse.domain.image.Image;
import com.example.muse.domain.image.ImageService;
import com.example.muse.domain.image.ImageType;
import com.example.muse.domain.like.LikesService;
import com.example.muse.domain.member.Member;
import com.example.muse.domain.review.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {
    private static final Set<String> ALLOWED_SORTS = Set.of("createdAt", "likes");
    private final ReviewRepository reviewRepository;
    private final BookService bookService;
    private final ImageService imageService;
    private final LikesService likesService;

    @Transactional
    public CreateReviewResponseDto createReview(Member member, Long bookId, CreateReviewRequestDto createReviewRequestDto, MultipartFile imageFile) {

        Book book = bookService.findById(bookId);
        // TODO: 기본이미지 결정 후 변경
        Image image = imageService.uploadImage(imageFile, ImageType.REVIEW, member);
        Review review = createReviewRequestDto.toEntity(image, member, book);
        review = reviewRepository.save(review);

        return CreateReviewResponseDto.from(review);
    }


    public GetReviewsResponseDto getMainReviews(Pageable pageable, Member member) {

        pageable = setDefaultSort(pageable);
        Page<Review> reviews = reviewRepository.findMainReviews(pageable);
        return GetReviewsResponseDto.from(reviews, member);
    }


    public GetReviewsResponseDto getUserReviews(Pageable pageable, UUID memberId, Member loggedInMember) {
        //TODO: memberId 예외처리
        pageable = setDefaultSort(pageable);
        Page<Review> reviews = reviewRepository.findByMemberId(pageable, memberId);
        return GetReviewsResponseDto.from(reviews, loggedInMember);
    }


    private Pageable setDefaultSort(Pageable pageable) {

        Sort sort = pageable.getSort();
        boolean validSort = sort.stream().allMatch(order -> ALLOWED_SORTS.contains(order.getProperty()));
        if (!validSort) {
            sort = Sort.by("createdAt").descending();
        }

        return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
    }


    @Transactional
    public CreateReviewResponseDto updateReview(
            Long reviewId,
            UpdateReviewRequestDto requestDto,
            MultipartFile imageFile,
            Member member) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 리뷰입니다."));

        if (!member.getId().equals(review.getMember().getId())) {
            throw new IllegalArgumentException("작성자만 수정할 수 있습니다.");
        }
        Image image = null;
        if (imageFile != null && !imageFile.isEmpty()) {
            Image originalImage = review.getImage();

            image = imageService.uploadImage(imageFile, ImageType.REVIEW, member);
            imageService.deleteImage(originalImage);
        }

        if (requestDto == null && image == null) {
            throw new IllegalArgumentException("수정할 내용이 없습니다.");
        }
        review.update(requestDto, image);

        return CreateReviewResponseDto.from(review);

    }

    @Transactional
    public void deleteReview(Long reviewId, Member member) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 리뷰입니다."));

        if (!member.getId().equals(review.getMember().getId())) {
            throw new IllegalArgumentException("작성자만 제거할 수 있습니다.");
        }

        Image image = review.getImage();

        imageService.deleteImage(image);
        reviewRepository.delete(review);
    }

    public GetLikedReviewsResponseDto getLikedReviews(Pageable pageable, Member member) {

        pageable = setDefaultSort(pageable);
        Page<Review> reviews = reviewRepository.findLikedReviews(pageable, member);

        return GetLikedReviewsResponseDto.from(reviews, member);
    }

    @Transactional
    public void reviewLike(Long reviewId, Member member) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 리뷰입니다."));

        likesService.createLike(review, member);
    }

    @Transactional
    public void reviewUnlike(Long reviewId, Member member) {

        likesService.unLikeReview(reviewId, member);
    }
}
