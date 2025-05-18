import React, { useEffect, useRef, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import styles from './ReviewCard.module.css';
import leftIcon from '../../assets/left.png';
import bookIcon from '../../assets/book.png';
import disLikesIcon from '../../assets/heart.png';
import menuIcon from '../../assets/menu-dots-vertical.png';
import likesIcon from '../../assets/heart_filled.png';
import reviewApi from '../../api/reviewApi';
import bookApi from '../../api/bookApi';
import { useSelector } from 'react-redux';
import DropBoxButton from '../button/DropBoxButton';

export default function ReviewCard({ review, book, user }) {

  const { reviewId, reviewImageUrl, content, reviewLikes, reviewIsLike } = review;
  const { bookId, bookImageUrl, title, author, publisher, bookLikes, bookIsLike } = book;
  const { userId, nickname, userImageUrl } = user;

  const [isOpen, setIsOpen] = useState(false);
  const [toggleCard, setToggleCard] = useState(false);

  const [likedReview, setLikedReview] = useState(reviewIsLike);
  const [likedBook, setLikedBook] = useState(bookIsLike);
  const [reviewLikesCount, setReviewLikesCount] = useState(reviewLikes);
  const [bookLikesCount, setBookLikesCount] = useState(bookLikes);

  const navigate = useNavigate();
  const modalRef = useRef(null);
  const memberId = useSelector((state) => state.auth.memberId);

  const toggleCardHandler = () => {
    setToggleCard(!toggleCard);
  };

  const clickTitleHandler = () => {
    navigate(`/books/${bookId}`);
  };

  const clickProfileHandler = () => {
    navigate(`/users/${userId}`);
  };

  const clickLikesHandler = async () => {
    try {
      if (!toggleCard) {
        setLikedReview(!likedReview);

        if (likedReview) {
          await reviewApi.postReviewLikes(reviewId);
          setReviewLikesCount(reviewLikesCount + 1);
        } else {
          await reviewApi.deleteReviewLikes(reviewId);
          setReviewLikesCount(reviewLikesCount - 1);
        }
      } else {
        setLikedBook(!likedBook);

        if (likedBook) {
          await bookApi.postBookLikes(bookId);
          setBookLikesCount(bookLikesCount + 1);
        } else {
          await bookApi.deleteBookLikes(bookId);
          setBookLikesCount(bookLikesCount - 1);
        }
      }
    } catch (error) {
      // TODO: 추후 에러 처리 보완
      console.error('좋아요 처리 실패');
    }
  };

  const clickMenuHandler = () => {
    setIsOpen(true);
  };

  const editReviewHandler = () => {
    navigate(`/reviews/${reviewId}/edit`);
  };

  const deleteReviewHandler = async () => {
    try {
      await reviewApi.deleteReview(reviewId);
      alert('리뷰 삭제 완료');
    } catch (error) {
      // TODO: 추후 에러 처리 보완
      console.error('리뷰 삭제 실패');
    }
  };

  useEffect(() => {
    const handleClickOutside = (event) => {
      if (modalRef.current && !modalRef.current.contains(event.target)) {
        setIsOpen(false);
      }
    };

    document.addEventListener('mousedown', handleClickOutside);

    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
    };
  }, [isOpen]);

  return (
    <div className={styles.card}>
      <div className={styles.topWrapper}>
        {!toggleCard ? (
          <>
            <div className={styles.imageWrapper}>
              <img src={reviewImageUrl} alt="cardImage" />
              <div className={styles.bookIconWrapper}>
                <img src={bookIcon} alt="bookIcon" className={styles.bookIcon} />
                <div className={styles.bookGrayText} onClick={toggleCardHandler}>
                  책 정보 보기
                </div>
              </div>
            </div>
            <div className={styles.content}>{content}</div>
          </>
        ) : (
          <>
            <div className={styles.imageWrapper}>
              <div className={styles.bookImageWrapper}>
                <img src={bookImageUrl} alt="cardImage" />
              </div>
              <div className={styles.leftIconWrapper} onClick={toggleCardHandler}>
                <img src={leftIcon} alt="leftIcon" className={styles.leftIcon} />
              </div>
            </div>
            <div className={styles.bookInfo}>
              <h1 className={styles.title} onClick={clickTitleHandler}>
                {title}
              </h1>
              <div className={styles.detailWrapper}>
                <div className={styles.flexBox2}>
                  <div>출판</div>
                  <div>작가</div>
                </div>
                <div className={styles.flexBox2}>
                  <div>{publisher}</div>
                  <div>{author}</div>
                </div>
              </div>
            </div>
          </>
        )}
      </div>
      <hr />
      <div className={styles.bottomWrapper}>
        <div className={styles.flexBox} onClick={clickProfileHandler}>
          <div className={styles.profileImageWrapper}>
            <img src={userImageUrl} alt="profileImage" />
          </div>
          <div className={styles.nickname}>{nickname}</div>
        </div>
        {!toggleCard ? (
          <div className={styles.flexBox}>
            {!likedReview ? (
              <img
                src={disLikesIcon}
                alt="disLikesIcon"
                className={styles.likeIcon}
                onClick={clickLikesHandler}
              />
            ) : (
              <img
                src={likesIcon}
                alt="likesIcon"
                className={styles.likeIcon}
                onClick={clickLikesHandler}
              />
            )}
            <div className={styles.grayText}>좋아요</div>
            <div className={styles.grayText}>{reviewLikesCount}</div>
            {memberId == userId && (
              <div className={styles.menuIconWrapper} ref={modalRef} onClick={clickMenuHandler}>
                <img src={menuIcon} alt="menuIcon" className={styles.menuIcon} />
                {isOpen && (
                  <DropBoxButton
                    clickHandler1={editReviewHandler}
                    clickHandler2={deleteReviewHandler}
                  ></DropBoxButton>
                )}
              </div>
            )}
          </div>
        ) : (
          <div className={styles.flexBox}>
            {!likedBook ? (
              <img
                src={disLikesIcon}
                alt="disLikesIcon"
                className={styles.likeIcon}
                onClick={clickLikesHandler}
              />
            ) : (
              <img
                src={likesIcon}
                alt="likesIcon"
                className={styles.likeIcon}
                onClick={clickLikesHandler}
              />
            )}
            <div className={styles.grayText}>좋아요</div>
            <div className={styles.grayText}>{bookLikesCount}</div>
          </div>
        )}
      </div>
    </div>
  );
}
