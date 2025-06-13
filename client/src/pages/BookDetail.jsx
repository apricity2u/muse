import styles from './BookDetail.module.css';
import { useEffect, useState } from 'react';
import SubTabButton from '../components/common/button/SubTabButton';
import reviewApi from '../api/reviewApi';
import bookApi from '../api/bookApi';
import { useParams } from 'react-router-dom';
import BookDetailContent from '../components/common/content/BookDetailContent';
import ReviewCard from '../components/common/card/ReviewCard';
import { useSelector } from 'react-redux';

export default function BookDetail() {
  const { bookId } = useParams();
  const nickname = useSelector((state) => state.auth.nickname);
  const userImageUrl = useSelector((state) => state.auth.imageUrl);
  const userId = useSelector((state) => state.auth.memberId);

  const [isBook, setIsBook] = useState(true);
  const [pageNo, setPageNo] = useState(1);
  const [sort, setSort] = useState('likes');
  const [bookInfo, setBookInfo] = useState({descriptionParagraphs: []});
  const [reviews, setReviews] = useState([]);
  const [totalReviews, setTotalReviews] = useState(0);

  useEffect(() => {
    const fetchData = async () => {
      const response = await reviewApi.getBookReviewLists(bookId, pageNo, sort);
      console.log(response.data.data.totalElements);

      setReviews(response.data.data.reviews);
      setTotalReviews(response.data.data.totalElements);
    };
    fetchData();
  }, [pageNo, sort]);

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
        </div>
      </div>
      <SubTabButton
        content1={'책소개'}
        content2={`리뷰(${totalReviews})`}
        setIsReview={setIsBook}
      />

      <div className={styles.subContainer}>
        {isBook ? (
          <div>
            {bookInfo.descriptionParagraphs.map((paragraph, idx) => (
              <p key={idx}>{paragraph}</p>
            ))}
          </div>
        ) : (
          reviews.map((review) => (
            <ReviewCard
              reviewDetail={{
                review: review,
                book: bookInfo,
                user: {
                  nickname,
                  profileImageUrl: userImageUrl,
                  memberId: userId,
                },
              }}
              setUserInfo:null /**/
              key={review.id}
            />
          ))
        )}
      </div>
    </div>
  );
}
