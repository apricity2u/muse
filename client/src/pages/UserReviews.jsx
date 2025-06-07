import styles from './UserReviews.module.css';
import ReviewCardLists from '../components/common/list/ReviewCardLists';
import BookCardLists from '../components/common/list/BookCardLists';
import SubTabButton from '../components/common/button/SubTabButton';
import AlignButton from '../components/common/button/AlignButton';
import { useEffect, useState } from 'react';
import reviewApi from '../api/reviewApi';
import bookApi from '../api/bookApi';
import { useParams } from 'react-router-dom';
import profileApi from '../api/profileApi';
import user from '../assets/user.png';

export default function UserReviews() {
  const { userId } = useParams();
  const [userInfo, setUserInfo] = useState({
    memberId: '',
    imageUrl: '',
    nickname: '',
    reviewCount: 0,
  });

  const [reviewCardLists, setReviewCardLists] = useState([]);
  const [bookCardLists, setBookCardLists] = useState([]);

  const [isReview, setIsReview] = useState(true);
  const [selected, setSelected] = useState('createdAt');
  const [page, setPage] = useState({
    pageNo: 1,
    totalPage: 1,
    hasPrevious: false,
    hasNext: false,
  });

  const { memberId, imageUrl, nickname, reviewCount } = userInfo;
  const { pageNo, totalPage, hasPrevious, hasNext } = page;

  const fetchUserInfo = async () => {
    try {
      const response = await profileApi.getProfile(userId);
      const data = response.data;
      const { memberId, imageUrl, nickname, reviewCount } = data;

      setUserInfo((prev) => ({
        ...prev,
        memberId: memberId,
        imageUrl: imageUrl,
        nickname: nickname,
        reviewCount: reviewCount,
      }));
      console.log(data);
    } catch (error) {
      // TODO 추후 에러처리 보완
      console.error(error);
    }
  };

  const fetchUserReviewLists = async () => {
    try {
      const response = await reviewApi.getUserReviewLists(userId, pageNo, selected);
      const data = response.data.data;
      const { totalPages, hasPrevious, hasNext, reviews } = data;

      setReviewCardLists(reviews);
      setPage((prev) => ({
        ...prev,
        totalPage: totalPages,
        hasPrevious: hasPrevious,
        hasNext: hasNext,
      }));
    } catch (error) {
      alert('리뷰 목록을 불러오는데 실패했습니다.');
      console.error(error);
    }
  };

  const fetchUserBookLists = async () => {
    try {
      const response = await bookApi.getUserBookLists(userId, pageNo, selected);
      const data = response.data.data;
      const { page, totalPages, hasPrevious, hasNext, books } = data;

      setBookCardLists(books);
      setPage((prev) => ({
        ...prev,
        pageNo: page,
        totalPage: totalPages,
        hasPrevious: hasPrevious,
        hasNext: hasNext,
      }));
    } catch (error) {
      alert('책 목록을 불러오는데 실패했습니다.');
      console.error(error);
    }
  };

  const sortListHandler = (sort) => {
    if (sort !== selected) {
      setSelected(sort);
    }
  };

  useEffect(() => {
    fetchUserInfo();

    if (userId) {
      isReview ? fetchUserReviewLists() : fetchUserBookLists();
    }
  }, [isReview, selected, reviewCount]);

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
          <SubTabButton content1="리뷰" content2="도서" setIsReview={setIsReview}></SubTabButton>
          <div className={styles.alignButton}>
            <AlignButton
              clickHandler1={() => sortListHandler('createdAt')}
              clickHandler2={() => sortListHandler('likes')}
              selected={selected === 'createdAt' ? '최신순' : '좋아요순'}
            ></AlignButton>
          </div>
        </div>
        {reviewCount === 0 ? (
          <div className={styles.noContentWrapper}>아직 작성한 리뷰가 없습니다.</div>
        ) : (
          <div className={styles.reviewWrapper}>
            {isReview ? (
              <ReviewCardLists reviewCardLists={reviewCardLists} reviewCount={reviewCount} setUserInfo={setUserInfo}></ReviewCardLists>
            ) : (
              <BookCardLists bookCardLists={bookCardLists}></BookCardLists>
            )}
          </div>
        )}
      </div>
    </div>
  );
}
