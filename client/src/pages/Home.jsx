import styles from './styles/Home.module.css';
import React, { useEffect, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import ReviewCardLists from '../components/list/ReviewCardLists';
import PaginationButton from '../components/button/PaginationButton';
import video from '../assets/main.mp4';
import scroll from '../assets/scroll.png';
import dog from '../assets/dog.jpg';
import harry from '../assets/harry.jpg';

export default function Home() {

  // 더미데이터 : 추후 삭제 예정
  const reviewCardLists = [
    {
      review: {
        reviewId: 1,
        reviewImageUrl: dog,
        content: '내용1',
        reviewLikes: 12,
        reviewIsLike: false,
      },

      book: {
        bookId: 1,
        bookImageUrl: harry,
        title: '해리포터1',
        author: '작가1',
        publisher: '출판사1',
        bookLikes: 34,
        bookIsLike: false,
      },

      user: {
        userId: 1,
        nickname: '삼상호',
        userImageUrl: dog,
      },
    },
    {
      review: {
        reviewId: 2,
        reviewImageUrl: dog,
        content: '내용2',
        reviewLikes: 56,
        reviewIsLike: false,
      },

      book: {
        bookId: 2,
        bookImageUrl: harry,
        title: '해리포터2',
        author: '작가1',
        publisher: '출판사2',
        bookLikes: 78,
        bookIsLike: false,
      },

      user: {
        userId: 2,
        nickname: '사상호',
        userImageUrl: dog,
      },
    },
    {
      review: {
        reviewId: 3,
        reviewImageUrl: dog,
        content: '내용3',
        reviewLikes: 90,
        reviewIsLike: false,
      },

      book: {
        bookId: 3,
        bookImageUrl: harry,
        title: '해리포터3',
        author: '작가1',
        publisher: '출판사3',
        bookLikes: 11,
        bookIsLike: false,
      },

      user: {
        userId: 3,
        nickname: '오상호',
        userImageUrl: dog,
      },
    },
  ];

  const navigate = useNavigate();
  const videoRef = useRef(null);

  useEffect(() => {
    if (videoRef.current) {
      videoRef.current.play().catch((e) => {
        console.warn('자동 재생 실패:', e);
      });
    }
  }, []);

  const clickCreateButton = () => {
    navigate('/reviews/create');
  };

  return (
    <div className={styles.container}>
      <div className={styles.videoWrapper}>
        <video
          ref={videoRef}
          src={video}
          className={styles.mainVideo}
          autoPlay
          muted
          playsInline
        ></video>
        <div className={styles.overlay}></div>
        <div className={styles.title}>
          당신의 다음 책,
          <br />
          누군가의 리뷰 속에 있어요
        </div>
        <div className={styles.scrollWrapper}>
          <div>scroll down</div>
          <img src={scroll} alt="scroll" className={styles.scroll} />
        </div>
      </div>
      <div className={styles.reviewWrapper}>
        <div className={styles.subTitle}>BookReview</div>
        <div className={styles.cardWrapper}>
          <PaginationButton side="left"></PaginationButton>
          <ReviewCardLists reviewCardLists={reviewCardLists}></ReviewCardLists>
          <PaginationButton></PaginationButton>
        </div>
        <div className={styles.button} onClick={clickCreateButton}>
          리뷰 작성하기
        </div>
      </div>
    </div>
  );
}
