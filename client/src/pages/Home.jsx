import styles from './Home.module.css';
import clsx from 'clsx';
import { useEffect, useRef, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import useSectionScroll from '../hook/useSectionScroll';
import ReviewCardLists from '../components/common/list/ReviewCardLists';
import PaginationButton from '../components/common/button/PaginationButton';
import reviewApi from '../api/reviewApi';
import scroll from '../assets/icons/scroll.png';
import video from '../assets/main.mp4';
import { useMediaQuery } from 'react-responsive';

export default function Home() {
  const navigate = useNavigate();

  const isMobile = useMediaQuery({ query: '(max-width: 900px)' });

  const videoRef = useRef(null);
  const reviewWrapperRef = useRef(null);

  const [reviewCardLists, setReviewCardLists] = useState([]);
  const [currentSection, setCurrentSection] = useState(0);
  const [page, setPage] = useState({
    pageNo: 1,
    hasPrevious: false,
    hasNext: false,
  });

  const { pageNo, hasPrevious, hasNext } = page;

  useSectionScroll({
    currentSection,
    setCurrentSection,
    videoRef,
    reviewWrapperRef,
  });

  useEffect(() => {
    const getReviewLists = async () => {
      try {
        const response = isMobile
          ? await reviewApi.getMainReviewLists(pageNo, 20)
          : await reviewApi.getMainReviewLists(pageNo);
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
      <div className={styles.sectionTop}>
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
      <div className={styles.sectionBottom}>
        <div ref={reviewWrapperRef} className={styles.reviewWrapper}>
          <div className={clsx(styles.subTitle, 'subTitle')}>BookReview</div>
          <div className={styles.cardWrapper}>
            {reviewCardLists?.length === 0 ? (
              <div className={styles.noContentWrapper}>가장 첫번째 리뷰어가 되어보세요!</div>
            ) : isMobile ? (
              <div className={styles.swipeContainer}>
                <ReviewCardLists reviewCardLists={reviewCardLists} type="main" size="big" />
              </div>
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
      <div className={styles.scrollBar}>
        <div
          className={clsx(
            styles.scrollIndicator,
            currentSection === 0 ? styles.scrollIndicatorTop : styles.scrollIndicatorBottom,
          )}
        ></div>
      </div>
    </div>
  );
}
