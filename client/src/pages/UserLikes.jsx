import styles from './UserLikes.module.css';
import ReviewCardLists from '../components/common/list/ReviewCardLists';
import SubTabButton from '../components/common/button/SubTabButton';
import AlignButton from '../components/common/button/AlignButton';
import { useSelector } from 'react-redux';
import { useEffect, useState } from 'react';
import reviewApi from '../api/reviewApi';
import bookApi from '../api/bookApi';
import BookCardLists from '../components/common/list/BookCardLists';

export default function UserLikes() {
  const user = useSelector((state) => state.auth);

  const [reviewCardLists, setReviewCardLists] = useState([]);
  const [bookCardLists, setBookCardLists] = useState([]);

  const [isReview, setIsReview] = useState(true);
  const [selected, setSelected] = useState('createdAt');
  const [page, setPage] = useState({
    totalPage: 0,
    pageNo: 1,
    hasPrevious: false,
    hasNext: false,
  });

  const { memberId, imageUrl, nickname } = user;
  const { totalPage, pageNo, hasPrevious, hasNext } = page;

  const fetchUserReviewLists = async () => {
    try {
      const response = await reviewApi.getLikedReviewLists(memberId, pageNo, selected);
      const data = response.data;
      const { totalPage, page, hasPrevious, hasNext, review } = data;

      setReviewCardLists(review);
      setPage((prev) => ({
        ...prev,
        totalPage: totalPage,
        pageNo: page,
        hasPrevious: hasPrevious,
        hasNext: hasNext,
      }));
    } catch (error) {
      alert('리뷰 목록을 불러오는데 실패했습니다.');
      console.log(error);
    }
  };

  const fetchUserBookLists = async () => {
    try {
      const response = await bookApi.getLikedBookLists(memberId, pageNo, selected);
      const data = response.data;
      const { totalPage, page, hasPrevious, hasNext, book } = data;

      setBookCardLists(book);
      setPage((prev) => ({
        ...prev,
        totalPage: totalPage,
        pageNo: page,
        hasPrevious: hasPrevious,
        hasNext: hasNext,
      }));
    } catch (error) {
      alert('책 목록을 불러오는데 실패했습니다.');
      console.log(error);
    }
  };

  const sortListHandler = (sort) => {
    if (sort !== selected) {
      setSelected(sort);
    }
  };

  useEffect(() => {
    if (memberId) {
      isReview ? fetchUserReviewLists() : fetchUserBookLists();
    }
  }, [isReview, selected]);

  return (
    <div className={styles.container}>
      <div className={styles.wrapper}>
        <div className={styles.profileWrapper}>
          <div className={styles.subTitle}>좋아요 표시한 컨텐츠</div>
        </div>
        <div className={styles.subHeader}>
          <SubTabButton content1="리뷰" content2="도서" setIsReview={setIsReview}></SubTabButton>
        </div>
        <div className={styles.detailWrapper}>
          <div className={styles.grayText}>
            총 {isReview ? reviewCardLists?.length || 0 : bookCardLists?.length || 0}건
          </div>
          <div className={styles.alignButton}>
            <AlignButton
              clickHandler1={() => sortListHandler('createdAt')}
              clickHandler2={() => sortListHandler('likes')}
              selected={selected === 'createdAt' ? '최신순' : '좋아요순'}
            ></AlignButton>
          </div>
        </div>
        <div className={styles.reviewWrapper}>
          {!totalPage ? (
            <div className={styles.noContentWrapper}>아직 좋아요한 내역이 없습니다.</div>
          ) : (
            <div className={styles.reviewWrapper}>
              {isReview ? (
                <ReviewCardLists reviewCardLists={reviewCardLists}></ReviewCardLists>
              ) : (
                <BookCardLists bookCardLists={bookCardLists}></BookCardLists>
              )}
            </div>
          )}{' '}
        </div>
      </div>
    </div>
  );
}
