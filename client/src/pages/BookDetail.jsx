import styles from './BookDetail.module.css';
import { useEffect, useState } from 'react';
import reviewApi from '../api/reviewApi';
import bookApi from '../api/bookApi';
import { useParams } from 'react-router-dom';
import BookDetailContent from '../components/common/content/BookDetailContent';
import { useSelector } from 'react-redux';
import ReviewCardLists from '../components/common/list/ReviewCardLists';
import BookDetailItem from '../components/common/item/BookDetailItem';

export default function BookDetail() {
  const { bookId } = useParams();
  const nickname = useSelector((state) => state.auth.nickname);
  const userImageUrl = useSelector((state) => state.auth.imageUrl);
  const userId = useSelector((state) => state.auth.memberId);

  const [pageNo, setPageNo] = useState(1);
  const [sort, setSort] = useState('likes');
  const [bookInfo, setBookInfo] = useState({ descriptionParagraphs: [] });
  const [reviews, setReviews] = useState([]);
  const [totalReviews, setTotalReviews] = useState(0);

  useEffect(() => {
    const fetchData = async () => {
      if (!bookId) return;
      const response = await reviewApi.getBookReviewLists(bookId, pageNo, sort);

      const wrapped = response.data.data.reviews.map((review) => ({
        review: review,
        book: {
          id: bookInfo.id,
          imageUrl: bookInfo.imageUrl,
          title: bookInfo.title,
          author: bookInfo.author,
          publisher: bookInfo.publisher,
          like: bookInfo.like,
          likeCount: bookInfo.likeCount,
        },
        user: {
          memberId: userId,
          nickname: nickname,
          profileImageUrl: userImageUrl,
        },
      }));
      setReviews(wrapped);
      setTotalReviews(response.data.data.totalElements);
    };
    fetchData();
  }, [pageNo, sort, bookInfo, nickname, userImageUrl, userId, bookId]);

  useEffect(() => {
    const fetchData = async () => {
      const response = await bookApi.getBook(bookId);
      setBookInfo(response.data.data);
    };
    fetchData();
  }, [bookId]);

  return (
    <div className={styles.wrapper}>
      <div className={styles.bookContainer}>
        <div className={styles.bookItem}>
          <img src={bookInfo.imageUrl} />
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
        <div>리뷰({totalReviews})</div>
      </div>
      <div className={styles.subContainerReview}>
        <ReviewCardLists
          reviewCardLists={reviews}
          type="bookPage"
          // size="big"
        />
      </div>
    </div>
  );
}
