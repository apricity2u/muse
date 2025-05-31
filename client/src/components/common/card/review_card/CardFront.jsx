import clsx from 'clsx';
import styles from './CardFront.module.css';
import { useSelector } from 'react-redux';
import { useEffect, useRef, useState } from 'react';
import bookIcon from '../../../../assets/icons/book.png';
import menuIcon from '../../../../assets/icons/menu-dots-vertical.png';
import disLikesIcon from '../../../../assets/icons/heart.png';
import likesIcon from '../../../../assets/icons/heart_filled.png';
import DropBoxButton from '../../button/DropBoxButton';
import reviewApi from '../../../../api/reviewApi';

export default function CardFront({
  review,
  user,
  toggleCardHandler,
  clickProfileHandler,
  toggleCard,
}) {
  const { reviewId, reviewImageUrl, content, reviewLikes, reviewIsLike } = review;
  const { userId, nickname, userImageUrl } = user;

  const [likedReview, setLikedReview] = useState(reviewIsLike);
  const [reviewLikesCount, setReviewLikesCount] = useState(reviewLikes);
  const [isOpen, setIsOpen] = useState(false);
  const [openModal, setOpenModal] = useState(false);

  const memberId = useSelector((state) => state.auth.memberId);

  const modalRef = useRef(null);

  useEffect(() => {
    setLikedReview(reviewIsLike);
    setReviewLikesCount(reviewLikes);
  }, [reviewIsLike, reviewLikes]);

  const clickLikesHandler = async () => {
    if (toggleCard) return;

    try {
      const next = !likedReview;
      setLikedReview(next);
      if (next) {
        await reviewApi.postReviewLikes(reviewId);
        setReviewLikesCount((prev) => prev + 1);
      } else {
        await reviewApi.deleteReviewLikes(reviewId);
        setReviewLikesCount((prev) => prev - 1);
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
    navigate(`/books/${bookId}/reviews/${reviewId}/edit`);
  };

  const deleteReviewHandler = async () => {
    try {
      await reviewApi.deleteReview(reviewId);
      alert('정상적으로 삭제되었습니다.');
    } catch (error) {
      // TODO: 추후 에러 처리 보완
      console.error('리뷰 삭제 실패');
    } finally {
      setOpenModal(false);
      setIsOpen(false);
    }
  };

  useEffect(() => {
    const handleClickOutside = (event) => {
      if (openModal) return;

      if (modalRef.current && !modalRef.current.contains(event.target)) {
        setIsOpen(false);
      }
    };

    document.addEventListener('mousedown', handleClickOutside);

    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
    };
  }, [isOpen, openModal]);

  return (
    <div className={styles.cardFront}>
      <div className={styles.topWrapper}>
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
      </div>
      <hr />
      <div className={styles.bottomWrapper}>
        <div className={clsx(styles.flexBox, styles.justifyStart)} onClick={clickProfileHandler}>
          <div className={styles.profileImageWrapper}>
            <img src={userImageUrl} alt="profileImage" />
          </div>
          <div className={styles.nickname}>{nickname}</div>
        </div>
        <div className={clsx(styles.flexBox, styles.justifyEnd)}>
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
                  openModal={openModal}
                  setOpenModal={setOpenModal}
                />
              )}
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
