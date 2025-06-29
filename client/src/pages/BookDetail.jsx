import styles from './BookDetail.module.css';
import { useEffect, useRef, useState } from 'react';
import reviewApi from '../api/reviewApi';
import bookApi from '../api/bookApi';
import { useParams } from 'react-router-dom';
import BookDetailContent from '../components/common/content/BookDetailContent';
import ReviewCardLists from '../components/common/list/ReviewCardLists';
import BookDetailItem from '../components/common/item/BookDetailItem';
import AlignButton from '../components/common/button/AlignButton';
import useScrollReview from '../hook/useScrollReview';

export default function BookDetail() {
  const { bookId } = useParams();

  const [page, setPage] = useState({
    pageNo: 1,
    totalPages: 1,
    totalElements: 0,
    hasPrevious: false,
    hasNext: false,
  });
  const [sort, setSort] = useState('createdAt');
  const [bookInfo, setBookInfo] = useState({ descriptionParagraphs: [] });
  const [reviews, setReviews] = useState([]);

  const { pageNo, totalElements, hasNext } = page;

  const paginationRef = useRef(null);
  const isFetchingRef = useRef(false);

  const fetchBookReviewLists = async () => {
    if (!bookId || isFetchingRef.current) return;
    isFetchingRef.current = true;

    try {
      const response = await reviewApi.getBookReviewLists(bookId, pageNo, sort);
      const data = response.data.data;
      const { page, totalPages, totalElements, hasPrevious, hasNext, book, reviews } = data;

      const wrapped = reviews.map((bookReview) => ({
        review: bookReview.review,
        book: { ...book },
        user: {
          memberId: bookReview.user.memberId,
          nickname: bookReview.user.nickname,
          profileImageUrl: bookReview.user.profileImageUrl,
        },
      }));
      setReviews((prev) => [...prev, ...wrapped]);
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

  useEffect(() => {
    const fetchData = async () => {
      const response = await bookApi.getBook(bookId);
      setBookInfo(response.data.data);
    };
    fetchData();
  }, [bookId]);

  const sortListHandler = (selected) => {
    if (selected !== sort) {
      setSort(selected);
      setPage({
        pageNo: 1,
        totalPages: 1,
        totalElements: 0,
        hasPrevious: false,
        hasNext: false,
      });
      setReviews([]);
      isFetchingRef.current = false;
    }
  };

  useScrollReview(
    sort,
    fetchBookReviewLists,
    isFetchingRef,
    paginationRef,
    pageNo,
    hasNext,
    setPage,
    setReviews,
  );

  return (
    <div className={styles.container}>
      <div className={styles.wrapper}>
        <div className={styles.bookContainer}>
          <div className={styles.bookItem}>
            <img src={bookInfo.imageUrl} alt='bookCover'/>
          </div>
          <div className={styles.bookItem}>
            <BookDetailContent bookDetail={bookInfo} />
            <div className={styles.bottomWrapper}>
              <BookDetailItem bookId={bookId} initialIsLike={bookInfo.like} />
            </div>
          </div>
        </div>
        <div className={styles.subHeader}>
          <div>도서 소개</div>
        </div>
        <div className={styles.subContainerBook}>
          {bookInfo.descriptionParagraphs.map((paragraph, idx) => (
            <p key={idx}>{paragraph}</p>
          ))}
        </div>
        <div className={styles.subHeader}>
          <div>리뷰({totalElements})</div>
          <div className={styles.alignButton}>
            <AlignButton
              clickHandler1={() => sortListHandler('createdAt')}
              clickHandler2={() => sortListHandler('likes')}
              selected={sort === 'createdAt' ? '최신순' : '좋아요순'}
            ></AlignButton>
          </div>
        </div>
        <div className={styles.subContainerReview}>
          <ReviewCardLists reviewCardLists={reviews} type="regular" />
        </div>
        {hasNext && <div ref={paginationRef}> </div>}
      </div>
    </div>
  );
}
