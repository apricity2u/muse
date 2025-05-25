import { useEffect, useRef, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import styles from './ReviewCard.module.css';
import leftIcon from '../../../assets/icons/left.png';
import bookIcon from '../../../assets/icons/book.png';
import disLikesIcon from '../../../assets/icons/heart.png';
import menuIcon from '../../../assets/icons/menu-dots-vertical.png';
import likesIcon from '../../../assets/icons/heart_filled.png';
import reviewApi from '../../../api/reviewApi';
import bookApi from '../../../api/bookApi';
import { useSelector } from 'react-redux';
import DropBoxButton from '../button/DropBoxButton';

export default function ReviewCard({ review, book, user }) {
  const { reviewId, reviewImageUrl, content, reviewLikes, reviewIsLike } = review;
  const { bookId, bookImageUrl, title, author, publisher, bookLikes, bookIsLike } = book;
  const { userId, nickname, userImageUrl } = user;

  const [isOpen, setIsOpen] = useState(false);
  const [openModal, setOpenModal] = useState(false);
  const [toggleCard, setToggleCard] = useState(false);

  const [likedReview, setLikedReview] = useState(reviewIsLike);
  const [likedBook, setLikedBook] = useState(bookIsLike);
  const [reviewLikesCount, setReviewLikesCount] = useState(reviewLikes);
  const [bookLikesCount, setBookLikesCount] = useState(bookLikes);

  const navigate = useNavigate();
  const modalRef = useRef(null);
  const memberId = useSelector((state) => state.auth.memberId);

  useEffect(() => {
    setLikedReview(reviewIsLike);
    setReviewLikesCount(reviewLikes);
  }, [reviewIsLike, reviewLikes]);

  useEffect(() => {
    setLikedBook(bookIsLike);
    setBookLikesCount(bookLikes);
  }, [bookIsLike, bookLikes]);

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
        const next = !likedReview;
        setLikedReview(next);
        if (next) {
          await reviewApi.postReviewLikes(reviewId);
          setReviewLikesCount((prev) => prev + 1);
        } else {
          await reviewApi.deleteReviewLikes(reviewId);
          setReviewLikesCount((prev) => prev - 1);
        }
      } else {
        const next = !likedBook;
        setLikedBook(next);

        if (next) {
          await bookApi.postBookLikes(bookId);
          setBookLikesCount((prev) => prev + 1);
        } else {
          await bookApi.deleteBookLikes(bookId);
          setBookLikesCount((prev) => prev - 1);
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
    <div className={`${styles.card} ${toggleCard && styles.flipped}`}>
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
          <div className={styles.flexBox} onClick={clickProfileHandler}>
            <div className={styles.profileImageWrapper}>
              <img src={userImageUrl} alt="profileImage" />
            </div>
            <div className={styles.nickname}>{nickname}</div>
          </div>
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
            {memberId === userId && (
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
      <div className={styles.cardBack}>
        <div className={styles.topWrapper}>
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
        </div>
      </div>
    </div>
  );
}
