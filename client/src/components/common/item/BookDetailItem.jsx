import styles from './BookDetailItem.module.css';
import CircleButton from '../button/CircleButton';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import bookApi from '../../../api/bookApi';

export default function BookDetailItem({ bookId, initialIsLike }) {
  const navigate = useNavigate();

  const [isLike, setIsLike] = useState(initialIsLike);

  const writeHandler = () => {
    navigate(`/reviews/create`, { state: { bookId: bookId } });
  };

  const copyLinkHandler = async (e) => {
    try {
      const url = `${window.location.origin}/books/${bookId}`;
      await navigator.clipboard.writeText(url);
      // TODO: 링크 복사 완료 alert창 디자인
      alert('링크 복사 완료');
    } catch (error) {
      // TODO: 추후 에러 처리 보완
      alert('링크 복사 실패');
    }
  };

  const likesHandler = async () => {
    try {
      if (!isLike) {
        await bookApi.postBookLikes(bookId);
      } else {
        await bookApi.deleteBookLikes(bookId);
      }
      setIsLike(!isLike);
    } catch (error) {
      // TODO: 추후 에러 처리 보완
      console.error('좋아요 처리 실패');
    }
  };

  return (
    <div className={styles.wrapper}>
      <CircleButton clickHandler={writeHandler}>리뷰 작성</CircleButton>
      <CircleButton clickHandler={copyLinkHandler}>링크 복사</CircleButton>
      <CircleButton clickHandler={likesHandler} isLike={isLike}>
        좋아요
      </CircleButton>
    </div>
  );
}
