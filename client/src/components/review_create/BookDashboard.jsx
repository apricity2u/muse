import styles from './BookDashboard.module.css';
import BookDetailContent from '../content/BookDetailContent';

export default function BookDashboard({ bookDetail }) {
  const { imageUrl } = bookDetail;

  return (
    <div className={styles.container}>
      <div className={styles.bookCoverWrapper}>
        <img src={imageUrl} alt="bookImageUrl" />
      </div>
      <div className={styles.bookContentWrapper}>
        <BookDetailContent bookDetail={bookDetail}></BookDetailContent>
      </div>
    </div>
  );
}
