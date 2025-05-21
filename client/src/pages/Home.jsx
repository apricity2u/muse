import styles from './styles/Home.module.css';
import clsx from 'clsx';
import React, { useEffect, useRef, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import ReviewCardLists from '../components/list/ReviewCardLists';
import PaginationButton from '../components/button/PaginationButton';
import reviewApi from '../api/reviewApi';
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
  const videoWrapperRef = useRef(null);
  const reviewWrapperRef = useRef(null);
  const isScrollingRef = useRef(false);

  const [currentSection, setCurrentSection] = useState(0);
  const [page, setPage] = useState({
    pageNo: 1,
    hasPrevious: false,
    hasNext: false,
  });
  const { pageNo, hasPrevious, hasNext } = page;
  // const [reviewCardLists, setReviewCardLists] = useState([]);

  useEffect(() => {
    if (videoRef.current) {
      videoRef.current.play().catch((e) => {
        console.warn('자동 재생 실패:', e);
      });
    }
    const handleWheel = (e) => {
      if (isScrollingRef.current) return;

      isScrollingRef.current = true;

      if (e.deltaY > 0 && currentSection === 0) {
        // 아래로 스크롤 → 리뷰 섹션
        reviewWrapperRef.current.scrollIntoView({ behavior: 'smooth' });
        setCurrentSection(1);
      } else if (e.deltaY < 0 && currentSection === 1) {
        // 위로 스크롤 → 비디오 섹션
        videoWrapperRef.current.scrollIntoView({ behavior: 'smooth' });
        setCurrentSection(0);
      }

      // 디바운싱 (스크롤이 끝나고 다시 가능하도록 1000ms)
      setTimeout(() => {
        isScrollingRef.current = false;
      }, 1000);
    };

    window.addEventListener('wheel', handleWheel, { passive: false });
    return () => window.removeEventListener('wheel', handleWheel);
  }, [currentSection]);

  useEffect(() => {
    const getReviewLists = async () => {
      try {
        const response = await reviewApi.getMainReviewLists(pageNo);
        const { page, hasPrevious, hasNext, data } = response.data;

        setReviewCardLists(data);
        setPage((prev) => ({ ...prev, pageNo: page, hasPrevious: hasPrevious, hasNext: hasNext }));
      } catch (error) {
        // TODO: 에러 처리 보완
        console.error('리뷰 불러오기 실패');
      }
    };
    getReviewLists();
  }, [pageNo]);

  const clickLeftHandler = () => {
    if (!hasPrevious) return;

    setPage((prev) => ({
      ...prev,
      pageNo: pageNo - 1,
    }));
  };

  const clickRightHandler = () => {
    if (!hasNext) return;

    setPage((prev) => ({
      ...prev,
      pageNo: pageNo + 1,
    }));
  };

  const clickCreateButton = () => {
    navigate('/reviews/create');
  };

  return (
    <div className={styles.container}>
      <div ref={videoWrapperRef} className={clsx(styles.videoWrapper, styles.section)}>
        <video
          ref={videoRef}
          src={video}
          className={styles.mainVideo}
          autoPlay
          muted
          playsInline
        ></video>
        <div className={styles.overlay}></div>
        <div className={clsx(styles.title, styles.textOverLay)}>
          당신의 다음 책,
          <br />
          누군가의 리뷰 속에 있어요
        </div>
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
      <div ref={reviewWrapperRef} className={clsx(styles.reviewWrapper, styles.section)}>
        <div className={styles.subTitle}>BookReview</div>
        <div className={styles.cardWrapper}>
          <PaginationButton clickHandler={clickLeftHandler} side="left"></PaginationButton>
          <ReviewCardLists reviewCardLists={reviewCardLists} type="main"></ReviewCardLists>
          <PaginationButton clickHandler={clickRightHandler}></PaginationButton>
        </div>
        <div className={styles.button} onClick={clickCreateButton}>
          리뷰 작성하기
        </div>
      </div>
    </div>
  );
}
