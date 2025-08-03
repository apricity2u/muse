import styles from './ReviewCreate.module.css';
import { useEffect, useState } from 'react';
import { useLocation, useNavigate, useParams } from 'react-router-dom';
import SearchBoard from '../components/common/board/SearchBoard';
import BookDashboard from '../components/review_create/BookDashboard';
import RoundButton from '../components/common/button/RoundButton';
import Card from '../components/review_create/Card';
import bookApi from '../api/bookApi';
import reviewApi from '../api/reviewApi';
import basic from '../assets/basic.jpg';
import { useSelector } from 'react-redux';
import ImageUploader from '../components/review_create/ImageUploader';

export default function ReviewCreate() {
  const navigate = useNavigate();

  const location = useLocation();
  const bookInfo = { ...location.state };
  const selectedBookId = bookInfo.bookId;

  const memberId = useSelector((state) => state.auth.memberId);
  const { bookId, reviewId } = useParams();

  const [isSubmitting, setIsSubmitting] = useState(false);
  const [isCreated, setIsCreated] = useState(false);
  const [review, setReview] = useState({
    image: null,
    imageUrl: basic,
    originalFileName: '',
    content: '',
  });
  const [bookDetail, setBookDetail] = useState({
    bookId: '',
    imageUrl: '',
    title: '',
    author: '',
    publisher: '',
    publishedDate: '',
    isbn: '',
  });

  const [updatedReview, setUpdatedReview] = useState({
    updatedImage: null,
    updatedContent: '',
  });

  const { imageUrl, originalFileName, image, content } = review;
  const { updatedImage, updatedContent } = updatedReview;

  useEffect(() => {
    if (selectedBookId) {
      clickResultHandler(selectedBookId);
    } else if (bookId && reviewId) {
      fetchReviewHandler(bookId, reviewId);
    }
  }, [bookId, reviewId, selectedBookId]);

  const clickResultHandler = async (e) => {
    const bookId = selectedBookId || e.target.id;

    try {
      const response = await bookApi.getBook(bookId);
      const data = response.data.data;

      setBookDetail(data);
      setIsCreated(true);
    } catch (error) {
      // TODO: 에러 보완
      alert('도서 검색 중 오류가 발생했습니다.');
      console.error(error);
    }
  };

  const fetchReviewHandler = async (bookId, reviewId) => {
    try {
      const response = await reviewApi.getReview(bookId, reviewId);
      const data = response.data.data;

      const { book, review } = data;

      if (review.memberId !== memberId) {
        throw new Error('');
      }

      setReview((prev) => ({
        ...prev,
        originalFileName: review.image.originalFileName,
        imageUrl: review.image.imageUrl,
        content: review.content,
      }));
      setBookDetail(book);
      setIsCreated(true);
    } catch (error) {
      // TODO: 에러 보완
      alert('리뷰 조회 중 오류가 발생했습니다.');
      console.error(error);
    }
  };

  const submitHandler = async () => {
    if (isSubmitting) return;

    if (!content.trim()) {
      alert('리뷰 내용을 입력해주세요.');
      return;
    }

    setIsSubmitting(true);

    if (!bookId && !reviewId) {
      try {
        const id = bookDetail.bookId;

        await reviewApi.postReview(id, review);
        navigate(`/books/${id}`);
      } catch (error) {
        // TODO: 에러 보완
        alert('리뷰 등록 중 오류가 발생했습니다.');
        console.error(error);
      } finally {
        setIsSubmitting(false);
      }
    } else {
      if (!updatedImage && !updatedContent) {
        alert('변경사항이 없습니다.');
        return;
      }

      try {
        await reviewApi.patchReview(reviewId, updatedReview);
        navigate(`/books/${bookId}`);
      } catch (error) {
        // TODO: 에러 보완
        alert('리뷰 수정 중 오류가 발생했습니다.');
        console.error(error);
      } finally {
        setIsSubmitting(false);
      }
    }
  };

  const inputHandler = (e) => {
    const inputValue = e.target.value.slice(0, 50);
    setReview((prev) => ({ ...prev, content: inputValue }));
    setUpdatedReview((prev) => ({ ...prev, updatedContent: inputValue }));
  };

  return (
    <div className={styles.container}>
      {!reviewId && !selectedBookId && !isCreated ? (
        <>
          <div className={styles.title}>글쓰기</div>
          <div className={styles.searchWrapper}>
            <SearchBoard clickHandler={clickResultHandler}></SearchBoard>
          </div>
        </>
      ) : (
        <div className={styles.container2}>
          <div className={styles.previewWrapper}>
            <h2>미리보기</h2>
            <Card review={review}></Card>
          </div>
          <div className={styles.reviewWrapper}>
            <div className={styles.wrapper}>
              <h2>도서</h2>
              <BookDashboard bookDetail={bookDetail}></BookDashboard>
            </div>
            <div className={styles.wrapper}>
              <ImageUploader
                originalFileName={originalFileName}
                image={image}
                imageUrl={imageUrl}
                setReview={setReview}
                setUpdatedReview={setUpdatedReview}
              ></ImageUploader>
            </div>
            <div className={styles.wrapper}>
              <h2>리뷰</h2>
              <textarea
                placeholder="이 책에 대한 한 줄 리뷰를 입력해주세요."
                value={content}
                maxLength={50}
                className={styles.textareaStyle}
                onChange={inputHandler}
              />
              <div className={styles.alignLeft}>{content.length}/50</div>
            </div>
            <div className={styles.alignLeft}>
              <RoundButton color="primary100" clickHandler={submitHandler}>
                등록
              </RoundButton>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
