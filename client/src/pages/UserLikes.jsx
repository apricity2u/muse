import styles from './UserLikes.module.css';
import ReviewCardLists from '../components/common/list/ReviewCardLists';
import SubTabButton from '../components/common/button/SubTabButton';
import AlignButton from '../components/common/button/AlignButton';
import { useSelector } from 'react-redux';
import { useRef, useState } from 'react';
import reviewApi from '../api/reviewApi';
import bookApi from '../api/bookApi';
import BookCardLists from '../components/common/list/BookCardLists';
import useScrollPagination from '../hook/useScrollPagination';

export default function UserLikes() {
  const user = useSelector((state) => state.auth);

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

  const { memberId } = user;
  const { pageNo, totalElements, hasNext } = page;

  const paginationRef = useRef(null);
  const isFetchingRef = useRef(false);

  const fetchUserReviewLists = async () => {
    if (isFetchingRef.current) return;
    isFetchingRef.current = true;

    try {
      const response = await reviewApi.getLikedReviewLists(pageNo, selected);
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
      const response = await bookApi.getLikedBookLists(pageNo, selected);
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
    memberId,
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
          <div className={styles.subTitle}>좋아요 표시한 컨텐츠</div>
        </div>
        <div className={styles.subHeader}>
          <SubTabButton
            content1="리뷰"
            content2="도서"
            setIsReview={setIsReview}
            setSelected={setSelected}
          ></SubTabButton>
        </div>
        <div className={styles.detailWrapper}>
          <div className={styles.grayText}>총 {totalElements}건</div>
          <div className={styles.alignButton}>
            <AlignButton
              clickHandler1={() => sortListHandler('createdAt')}
              clickHandler2={() => sortListHandler('likes')}
              selected={selected === 'createdAt' ? '최신순' : '좋아요순'}
            ></AlignButton>
          </div>
        </div>
        {totalElements === 0 ? (
          <div className={styles.noContentWrapper}>아직 좋아요한 내역이 없습니다.</div>
        ) : (
          <div className={styles.reviewWrapper}>
            {isReview ? (
              <ReviewCardLists reviewCardLists={reviewCardLists}></ReviewCardLists>
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
