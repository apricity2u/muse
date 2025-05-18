import React, { useEffect, useRef, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import styles from './ReviewCard.module.css';
import dog from '../../assets/dog.jpg';
import harry from '../../assets/harry.jpg';
import bookIcon from '../../assets/book.png';
import disLikesIcon from '../../assets/heart.png';
import menuIcon from '../../assets/menu-dots-vertical.png';
import likesIcon from '../../assets/heart_filled.png';
import reviewApi from '../../api/reviewApi';
import { useSelector } from 'react-redux';
import DropBoxButton from '../button/DropBoxButton';

export default function ReviewCard({ review, book, user }) {

  const { reviewId, reviewImageUrl, content, reviewLikes, reviewIsLike } = review;
  const { bookId, bookImageUrl, title, author, publisher, bookLikes, bookIsLike } = book;
  const { userId, nickname, userImageUrl } = user;

  const [isOnMouse, setIsOnMouse] = useState(false);
  const [isOpen, setIsOpen] = useState(false);

  const [isLiked, setIsLiked] = useState(reviewIsLike);
  const [reviewLikesCount, setReviewLikesCount] = useState(reviewLikes);
  const [bookLikesCount, setBookLikesCount] = useState(bookLikes);

  const navigate = useNavigate();
  const modalRef = useRef(null);
  const memberId = useSelector((state) => state.auth.memberId);

  const mouseOverHandler = () => {
    setIsOnMouse(!isOnMouse);
  };

  const clickProfileHandler = () => {
    navigate(`/users/${userId}`);
  };

  const clickLikesHandler = async () => {
    try {
      setIsLiked(!isLiked);

      if (isLiked) {
        await reviewApi.postReviewLikes(reviewId);
        setReviewLikesCount(reviewLikesCount + 1);
      } else {
        await reviewApi.deleteReviewLikes(reviewId);
        setReviewLikesCount(reviewLikesCount - 1);
      }
    } catch (error) {
      // TODO: 추후 에러 처리 보완
      console.error('리뷰 좋아요 처리 실패');
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
        <div className={styles.imageWrapper}>
          <img src={reviewImageUrl} alt="cardImage" />
          <div
            className={styles.bookIconWrapper}
            onMouseEnter={mouseOverHandler}
            onMouseLeave={mouseOverHandler}
          >
            {!isOnMouse ? (
              <img src={bookIcon} alt="bookIcon" className={styles.bookIcon} />
            ) : (
              <div className={styles.bookGrayText}>책 정보 보기</div>
            )}
          </div>
        </div>
        <div className={styles.content}>{content}</div>
      </div>
      <hr />
      <div className={styles.bottomWrapper}>
        <div className={styles.flexBox} onClick={clickProfileHandler}>
          <div className={styles.profileImageWrapper}>
            <img src={userImageUrl} alt="profileImage" />
          </div>
          <div className={styles.nickname}>{nickname}</div>
        </div>
        <div className={styles.flexBox}>
          {!isLiked ? (
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
      </div>
    </div>
  );
}
