import styles from './BookCard.module.css';
import BookDetailItem from '../item/BookDetailItem';
import { useNavigate } from 'react-router-dom';

export default function BookCard({ bookDetail }) {
  const navigate = useNavigate();

  const { id, imageUrl, title, publisher, author, like } = bookDetail;

  const clickCardHandler = () => {
    navigate(`/books/${id}`);
  };

  return (
    <>
      <div className={styles.wrapper}>
        <div className={styles.bookImageWrapper} onClick={clickCardHandler}>
          <img src={imageUrl} alt="bookImage" />
        </div>
        <div className={styles.content}>
          <h1 className={styles.title} onClick={clickCardHandler}>
            {title}
          </h1>
          <div className={styles.detailWrapper}>
            <div className={styles.flexBox}>
              <div className={styles.subTitle}>출판</div>
              <div className={styles.subContent}>{publisher}</div>
            </div>
            <div className={styles.flexBox}>
              <div className={styles.subTitle}>작가</div>
              <div className={styles.subContent}>{author}</div>
            </div>
          </div>
          <div className={styles.bottomWrapper}>
            <BookDetailItem bookId={id} initialIsLike={like}></BookDetailItem>
          </div>
        </div>
      </div>
    </>
  );
}
