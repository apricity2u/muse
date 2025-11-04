package com.example.muse.domain.review;

import com.example.muse.domain.book.Book;
import com.example.muse.domain.book.BookService;
import com.example.muse.domain.book.dto.BookDto;
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

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {
    public static final Set<String> ALLOWED_SORTS = Set.of("createdAt", "likes");
    private final ReviewRepository reviewRepository;
    private final BookService bookService;
    private final ImageService imageService;
    private final LikesService likesService;

    @Transactional
    public CreateReviewResponseDto createReview(Member member, Long bookId, CreateReviewRequestDto createReviewRequestDto, MultipartFile imageFile) {

        Book book = bookService.findById(bookId);
        Image image = null;
        if (imageFile != null && !imageFile.isEmpty()) {
            image = imageService.uploadImage(imageFile, ImageType.REVIEW, member);
        }

        Review review = createReviewRequestDto.toEntity(image, member, book);
        review = reviewRepository.save(review);

        return CreateReviewResponseDto.from(review);
    }


    public GetReviewCardsResponseDto getMainReviews(Pageable pageable, Member member) {

        pageable = setDefaultSort(pageable);
        Page<Review> reviews = reviewRepository.findMainReviews(pageable);
        return GetReviewCardsResponseDto.from(reviews, member);
    }


    public GetReviewCardsResponseDto getUserReviews(Pageable pageable, UUID memberId, Member loggedInMember) {
        //TODO: memberId 예외처리
        pageable = setDefaultSort(pageable);
        boolean isLikesSort = pageable.getSort().stream().anyMatch(order -> order.getProperty().equals("likes"));
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());

        Page<Review> reviews = isLikesSort ?
                reviewRepository.findByMemberIdOrderByLikesDesc(pageable, memberId) :
                reviewRepository.findByMemberIdOrderByDateDesc(pageable, memberId);
        return GetReviewCardsResponseDto.from(reviews, loggedInMember);
    }


    @Transactional
    public CreateReviewResponseDto updateReview(Long reviewId, UpdateReviewRequestDto requestDto, MultipartFile imageFile, Member member) {

        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 리뷰입니다."));
        if (!member.getId().equals(review.getMember().getId())) {
            throw new IllegalArgumentException("작성자만 수정할 수 있습니다.");
        }
        Image image = null;
        if (imageFile != null && !imageFile.isEmpty()) {
            Image originalImage = review.getImage();

            image = imageService.uploadImage(imageFile, ImageType.REVIEW, member);
            if (originalImage != null) {
                imageService.deleteImage(originalImage);
            }
        }

        if (requestDto == null && image == null) {
            throw new IllegalArgumentException("수정할 내용이 없습니다.");
        }
        review.update(requestDto, image);

        return CreateReviewResponseDto.from(review);

    }

    @Transactional
    public void deleteReview(Long reviewId, Member member) {

        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 리뷰입니다."));

        if (!member.getId().equals(review.getMember().getId())) {
            throw new IllegalArgumentException("작성자만 제거할 수 있습니다.");
        }

        Image image = review.getImage();

        imageService.deleteImage(image);
        reviewRepository.delete(review);
    }

    public GetLikedReviewsResponseDto getLikedReviews(Pageable pageable, Member member) {

        pageable = setDefaultSort(pageable);
        boolean isLikesSort = pageable.getSort().stream().anyMatch(order -> order.getProperty().equals("likes"));
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());

        Page<Review> reviewPage = isLikesSort ?
                reviewRepository.findLikedReviewsOrderByLikesDesc(member.getId(), pageable) :
                reviewRepository.findLikedReviewsByMemberIdOrderByCreatedAtDesc(member.getId(), pageable);

        return GetLikedReviewsResponseDto.from(reviewPage, member);
    }

    @Transactional
    public void reviewLike(Long reviewId, Member member) {

        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 리뷰입니다."));

        likesService.createLike(review, member);
    }

    @Transactional
    public void reviewUnlike(Long reviewId, Member member) {

        likesService.unLikeReview(reviewId, member);
    }

    public GetReviewsResponseDto getBookReviews(Long bookId, Pageable pageable, Member member) {

        pageable = setDefaultSort(pageable);
        boolean isLikesSort = pageable.getSort().stream().anyMatch(order -> order.getProperty().equals("likes"));
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());

        Page<Review> reviews = isLikesSort ?
                reviewRepository.findByBookIdOrderByLikesDesc(pageable, bookId) :
                reviewRepository.findByBookIdOrderByDateDesc(pageable, bookId);

        return GetReviewsResponseDto.from(reviews, member);
    }


    public GetReviewDetailResponseDto getReview(Long bookId, Long reviewId, Member member) {

        Review review = reviewRepository.findReviewWithBookById(reviewId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 리뷰입니다."));
        if (!Objects.equals(review.getBook().getId(), bookId)) {
            throw new IllegalArgumentException("존재하지 않는 도서입니다.");
        }
        ReviewDetailDto reviewDto = ReviewDetailDto.from(review, member);
        BookDto bookDto = BookDto.from(review.getBook(), member);

        return GetReviewDetailResponseDto.from(bookDto, reviewDto);
    }

    private Pageable setDefaultSort(Pageable pageable) {

        Sort sort = pageable.getSort();
        boolean validSort = sort.stream().allMatch(order -> ALLOWED_SORTS.contains(order.getProperty()));
        if (!validSort) {
            sort = Sort.by("createdAt").descending();
        }

        return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
    }
}