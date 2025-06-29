import styles from './UserReviews.module.css';
import ReviewCardLists from '../components/common/list/ReviewCardLists';
import BookCardLists from '../components/common/list/BookCardLists';
import SubTabButton from '../components/common/button/SubTabButton';
import AlignButton from '../components/common/button/AlignButton';
import { useEffect, useRef, useState } from 'react';
import reviewApi from '../api/reviewApi';
import bookApi from '../api/bookApi';
import { useParams } from 'react-router-dom';
import profileApi from '../api/profileApi';
import user from '../assets/user.png';
import useScrollPagination from '../hook/useScrollPagination';

export default function UserReviews() {
  const { userId } = useParams();
  const [userInfo, setUserInfo] = useState({
    memberId: '',
    imageUrl: '',
    nickname: '',
    reviewCount: '',
  });

  const [reviewCardLists, setReviewCardLists] = useState([]);
  const [bookCardLists, setBookCardLists] = useState([]);

  const [isReview, setIsReview] = useState(true);
  const [selected, setSelected] = useState('createdAt');
  const [page, setPage] = useState({
    pageNo: 1,
    totalPages: 1,
    totalElements: 0,
    hasPrevious: false,
    hasNext: false,
  });

  const { imageUrl, nickname, reviewCount } = userInfo;
  const { pageNo, totalElements, hasNext } = page;

  const paginationRef = useRef(null);
  const isFetchingRef = useRef(false);

  useEffect(() => {
    const fetchUserInfo = async () => {
      try {
        const response = await profileApi.getProfile(userId);
        const data = response.data;
        const { memberId, profileImageUrl, nickname, reviewCount } = data;

        setUserInfo((prev) => ({
          ...prev,
          memberId: memberId,
          imageUrl: profileImageUrl,
          nickname: nickname,
          reviewCount: reviewCount,
        }));
      } catch (error) {
        // TODO 추후 에러처리 보완
        console.error(error);
      }
    };
    fetchUserInfo();
  }, [userId]);

  const fetchUserReviewLists = async () => {
    if (isFetchingRef.current) return;
    isFetchingRef.current = true;

    try {
      const response = await reviewApi.getUserReviewLists(userId, pageNo, selected);
      const data = response.data.data;
      const { page, totalPages, totalElements, hasPrevious, hasNext, reviews } = data;

      setReviewCardLists((prev) => [...prev, ...reviews]);
      setPage((prev) => ({
        ...prev,
        pageNo: page + 1,
        totalPages: totalPages,
        totalElements: totalElements,
        hasPrevious: hasPrevious,
        hasNext: hasNext,
      }));
    } catch (error) {
      alert('리뷰 목록을 불러오는데 실패했습니다.');
      console.error(error);
    } finally {
      isFetchingRef.current = false;
    }
  };

  const fetchUserBookLists = async () => {
    if (isFetchingRef.current) return;
    isFetchingRef.current = true;

    try {
      const response = await bookApi.getUserBookLists(userId, pageNo, selected);
      const data = response.data.data;
      const { page, totalPages, totalElements, hasPrevious, hasNext, books } = data;

      setBookCardLists((prev) => [...prev, ...books]);
      setPage((prev) => ({
        ...prev,
        pageNo: page + 1,
        totalPages: totalPages,
        totalElements: totalElements,
        hasPrevious: hasPrevious,
        hasNext: hasNext,
      }));
    } catch (error) {
      alert('책 목록을 불러오는데 실패했습니다.');
      console.error(error);
    } finally {
      isFetchingRef.current = false;
    }
  };

  const sortListHandler = (sort) => {
    if (sort !== selected) {
      setSelected(sort);
      setPage({
        pageNo: 1,
        totalPages: 1,
        totalElements: 0,
        hasPrevious: false,
        hasNext: false,
      });
      setReviewCardLists([]);
      setBookCardLists([]);
      isFetchingRef.current = false;
    }
  };

  useScrollPagination(
    userId,
    isReview,
    selected,
    fetchUserReviewLists,
    fetchUserBookLists,
    isFetchingRef,
    paginationRef,
    page,
    setPage,
    setReviewCardLists,
    setBookCardLists,
  );

  return (
    <div className={styles.container}>
      <div className={styles.wrapper}>
        <div className={styles.profileWrapper}>
          <div className={styles.imageWrapper}>
            <img src={imageUrl || user} alt="userImage" className={styles.userImage} />
          </div>
          <div className={styles.rightWrapper}>
            <div className={styles.nickname}>{nickname}</div>
            <div className={styles.grayText}>작성한 리뷰 {reviewCount}건</div>
          </div>
        </div>
        <div className={styles.subHeader}>
          <SubTabButton
            content1="리뷰"
            content2="도서"
            setIsReview={setIsReview}
            setSelected={setSelected}
          ></SubTabButton>
          <div className={styles.alignButton}>
            <AlignButton
              clickHandler1={() => sortListHandler('createdAt')}
              clickHandler2={() => sortListHandler('likes')}
              selected={selected === 'createdAt' ? '최신순' : '좋아요순'}
            ></AlignButton>
          </div>
        </div>
        {totalElements === 0 ? (
          <div className={styles.noContentWrapper}>아직 작성한 리뷰가 없습니다.</div>
        ) : (
          <div className={styles.reviewWrapper}>
            {isReview ? (
              <ReviewCardLists
                reviewCardLists={reviewCardLists}
                setUserInfo={setUserInfo}
              ></ReviewCardLists>
            ) : (
              <BookCardLists bookCardLists={bookCardLists}></BookCardLists>
            )}
          </div>
        )}
        {hasNext && <div ref={paginationRef}> </div>}
      </div>
    </div>
  );
}
