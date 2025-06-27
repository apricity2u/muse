package com.example.muse.domain.review;

import com.example.muse.domain.book.Book;
import com.example.muse.domain.book.BookRepository;
import com.example.muse.domain.book.dto.BookDto;
import com.example.muse.domain.image.Image;
import com.example.muse.domain.image.ImageRepository;
import com.example.muse.domain.image.ImageService;
import com.example.muse.domain.image.ImageType;
import com.example.muse.domain.like.LikesService;
import com.example.muse.domain.member.Member;
import com.example.muse.domain.member.MemberRepository;
import com.example.muse.domain.member.dto.MemberProfileDto;
import com.example.muse.domain.review.dto.*;
import com.example.muse.global.common.exception.CustomBadRequestException;
import com.example.muse.global.common.exception.CustomNotFoundException;
import com.example.muse.global.common.exception.CustomUnauthorizedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ReviewService {
    public static final Set<String> ALLOWED_SORTS = Set.of("createdAt", "likes");
    private final ReviewRepository reviewRepository;
    private final ImageRepository imageRepository;
    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;
    private final ImageService imageService;
    private final LikesService likesService;

    @Transactional
    public CreateReviewResponseDto createReview(UUID memberId, Long bookId, CreateReviewRequestDto createReviewRequestDto, MultipartFile imageFile) {

        Member member = memberId == null ? null : memberRepository.getReferenceById(memberId);
        Book book = bookRepository.getReferenceById(bookId);
        Image image = null;
        if (imageFile != null && !imageFile.isEmpty()) {
            image = imageService.uploadImage(imageFile, ImageType.REVIEW, member);
        }

        Review review = createReviewRequestDto.toEntity(image, member, book);
        review = reviewRepository.save(review);

        return CreateReviewResponseDto.from(review);
    }


    public GetReviewCardsResponseDto getMainReviews(Pageable pageable, UUID memberId) {
        
        pageable = setDefaultSort(pageable);
        Page<ReviewCardDto> reviewCardDtos = reviewRepository.findMainReviews(memberId, pageable);
        List<ReviewCardResponseDto> cards = reviewCardDtos.getContent().stream()
                .map(ReviewCardResponseDto::from)
                .toList();


        return GetReviewCardsResponseDto.builder()
                .reviews(cards)
                .page(reviewCardDtos.getNumber() + 1)
                .totalPages(reviewCardDtos.getTotalPages())
                .hasNext(reviewCardDtos.hasNext())
                .hasPrevious(reviewCardDtos.hasPrevious())
                .totalElements(reviewCardDtos.getTotalElements())
                .build();
    }


    public GetReviewCardsResponseDto getUserReviews(Pageable pageable, UUID memberId, UUID authMemberId) {

        Member authMember = authMemberId == null ? null : memberRepository.getReferenceById(authMemberId);
        pageable = setDefaultSort(pageable);
        boolean isLikesSort = pageable.getSort().stream().anyMatch(order -> order.getProperty().equals("likes"));
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());

        Page<Review> reviews = isLikesSort ?
                reviewRepository.findByMemberIdOrderByLikesDesc(pageable, memberId) :
                reviewRepository.findByMemberIdOrderByDateDesc(pageable, memberId);

        Map<UUID, String> profileImageMap = getProfileImageMap(reviews.getContent());

        return GetReviewCardsResponseDto.from(reviews, authMember, profileImageMap);
    }


    @Transactional
    public CreateReviewResponseDto updateReview(Long reviewId, UpdateReviewRequestDto requestDto, MultipartFile imageFile, UUID memberId) {

        Member member = memberId == null ? null : memberRepository.getReferenceById(memberId);
        Review review = reviewRepository.findById(reviewId).orElseThrow(CustomNotFoundException::new);

        if (!memberId.equals(review.getMember().getId())) {
            throw new CustomUnauthorizedException();
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
            throw new CustomBadRequestException();
        }
        review.update(requestDto, image);

        return CreateReviewResponseDto.from(review);

    }

    @Transactional
    public void deleteReview(Long reviewId, UUID memberId) {

        Review review = reviewRepository.findById(reviewId).orElseThrow(CustomNotFoundException::new);

        if (!memberId.equals(review.getMember().getId())) {
            throw new CustomUnauthorizedException();
        }

        Optional.ofNullable(review.getImage())
                .ifPresent(imageService::deleteImage); //TODO: 이벤트 기반으로 변경

        reviewRepository.delete(review);
    }

    public GetLikedReviewsResponseDto getLikedReviews(Pageable pageable, UUID memberId) {

        Member member = memberId == null ? null : memberRepository.getReferenceById(memberId);
        pageable = setDefaultSort(pageable);
        boolean isLikesSort = pageable.getSort().stream().anyMatch(order -> order.getProperty().equals("likes"));
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());

        Page<Review> reviewPage = isLikesSort ?
                reviewRepository.findLikedReviewsOrderByLikesDesc(memberId, pageable) :
                reviewRepository.findLikedReviewsByMemberIdOrderByCreatedAtDesc(memberId, pageable);
        Image profileImage = imageRepository.findProfileImageByMemberId(memberId)
                .orElse(null);

        return GetLikedReviewsResponseDto.from(reviewPage, member, profileImage.getImageUrl());
    }

    @Transactional
    public void reviewLike(Long reviewId, UUID memberId) {

        Member member = memberId == null ? null : memberRepository.getReferenceById(memberId);
        Review review = reviewRepository.getReferenceById(reviewId);

        likesService.createLike(review, member);
    }

    @Transactional
    public void reviewUnlike(Long reviewId, UUID memberId) {

        likesService.unLikeReview(reviewId, memberId);
    }

    public BookReviewsResponseDto getBookWithReviews(Long bookId, Pageable pageable, UUID memberId) {
        Member member = memberId == null ? null : memberRepository.getReferenceById(memberId);
        pageable = setDefaultSort(pageable);
        boolean isLikesSort = pageable.getSort().stream().anyMatch(order -> order.getProperty().equals("likes"));
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());


        Book book = bookRepository.getReferenceById(bookId);
        Page<Review> reviews = isLikesSort
                ? reviewRepository.findByBookIdOrderByLikesDesc(pageable, bookId)
                : reviewRepository.findByBookIdOrderByDateDesc(pageable, bookId);

        Map<UUID, String> profileImageMap = getProfileImageMap(reviews.getContent());
        BookDto bookDto = BookDto.from(book, member);

        List<ReviewWithUserDto> reviewWithUserList = reviews.getContent().stream()
                .map(review -> ReviewWithUserDto.builder()
                        .review(
                                ReviewDto.from(review, member)
                        )
                        .user(
                                MemberProfileDto.from(
                                        review.getMember(),
                                        profileImageMap.get(review.getMember().getId())
                                )
                        )
                        .build()
                )
                .toList();

        return BookReviewsResponseDto.builder()
                .book(bookDto)
                .reviews(reviewWithUserList)
                .page(reviews.getNumber() + 1)
                .totalPages(reviews.getTotalPages())
                .hasNext(reviews.hasNext())
                .hasPrevious(reviews.hasPrevious())
                .totalElements(reviews.getTotalElements())
                .build();
    }


    public GetReviewDetailResponseDto getReview(Long bookId, Long reviewId, UUID memberId) {

        Member member = memberId == null ? null : memberRepository.getReferenceById(memberId);
        Review review = reviewRepository.findReviewWithBookById(reviewId).orElseThrow(CustomNotFoundException::new);
        if (!review.getBook().getId().equals(bookId)) {
            throw new CustomNotFoundException();
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

    private Map<UUID, String> getProfileImageMap(List<Review> reviews) {

        List<UUID> memberIds = reviews.stream()
                .map(review -> review.getMember().getId())
                .distinct()
                .toList();

        return imageRepository
                .findAllByMemberIdInAndImageType(memberIds, ImageType.PROFILE)
                .stream()
                .collect(Collectors.toMap(
                        image -> image.getMember().getId(),
                        Image::getImageUrl
                ));
    }
}