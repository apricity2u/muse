import clsx from 'clsx';
import styles from './CardBack.module.css';
import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import leftIcon from '../../../../assets/icons/left.png';
import disLikesIcon from '../../../../assets/icons/heart.png';
import likesIcon from '../../../../assets/icons/heart_filled.png';
import bookApi from '../../../../api/bookApi';

export default function CardBack({
  book,
  user,
  toggleCardHandler,
  clickProfileHandler,
  toggleCard,
}) {
  const { id, imageUrl, title, author, publisher, likeCount, like } = book;
  const { nickname, profileImageUrl } = user;

  const [likedBook, setLikedBook] = useState(like);
  const [bookLikeCount, setBookLikeCount] = useState(likeCount);

  const navigate = useNavigate();

  useEffect(() => {
    setLikedBook(like);
    setBookLikeCount(likeCount);
  }, [like, likeCount]);

  const clickTitleHandler = () => {
    navigate(`/books/${id}`);
  };

  const clickLikesHandler = async () => {
    if (!toggleCard) return;

    try {
      const next = !likedBook;
      setLikedBook(next);

      if (next) {
        await bookApi.postBookLikes(id);
        setBookLikeCount((prev) => prev + 1);
      } else {
        await bookApi.deleteBookLikes(id);
        setBookLikeCount((prev) => prev - 1);
      }
    } catch (error) {
      // TODO: 추후 에러 처리 보완
      console.error('좋아요 처리 실패');
    }
  };

  return (
    <div className={styles.cardBack}>
      <div className={styles.topWrapper}>
        <div className={styles.imageWrapper}>
          <div className={styles.bookImageWrapper}>
            <img src={imageUrl} alt="cardImage" />
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
        <div className={clsx(styles.flexBox, styles.justifyStart)} onClick={clickProfileHandler}>
          <div className={styles.profileImageWrapper}>
            <img src={profileImageUrl} alt="profileImage" />
          </div>
          <div className={styles.nickname}>{nickname}</div>
        </div>
        <div className={clsx(styles.flexBox, styles.justifyEnd)}>
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
          <div className={styles.grayText}>{bookLikeCount}</div>
        </div>
      </div>
    </div>
  );
}
