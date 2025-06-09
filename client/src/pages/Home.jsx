import styles from './Home.module.css';
import clsx from 'clsx';
import { useEffect, useRef, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import ReviewCardLists from '../components/common/list/ReviewCardLists';
import PaginationButton from '../components/common/button/PaginationButton';
import reviewApi from '../api/reviewApi';
import scroll from '../assets/icons/scroll.png';
import video from '../assets/main.mp4';

export default function Home() {
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
  const [reviewCardLists, setReviewCardLists] = useState([]);

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
        const { hasPrevious, hasNext, reviews } = response.data.data;

        setReviewCardLists(reviews);
        setPage((prev) => ({ ...prev, hasPrevious, hasNext }));
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
      <div className={styles.section}>
        <div ref={videoWrapperRef} className={styles.videoWrapper}>
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
      </div>
      <div className={styles.section}>
        <div ref={reviewWrapperRef} className={styles.reviewWrapper}>
          <div className={styles.subTitle}>BookReview</div>
          <div className={styles.cardWrapper}>
            {reviewCardLists?.length === 0 ? (
              <div className={styles.noContentWrapper}>가장 첫번째 리뷰어가 되어보세요!</div>
            ) : (
              <>
                <PaginationButton clickHandler={clickLeftHandler} side="left"></PaginationButton>
                <ReviewCardLists
                  reviewCardLists={reviewCardLists}
                  type="main"
                  size="big"
                ></ReviewCardLists>
                <PaginationButton clickHandler={clickRightHandler}></PaginationButton>
              </>
            )}
          </div>
          <div className={styles.button} onClick={clickCreateButton}>
            리뷰 작성하기
          </div>
        </div>
      </div>
    </div>
  );
}
