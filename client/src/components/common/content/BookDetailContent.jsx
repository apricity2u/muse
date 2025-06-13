import styles from './BookDetailContent.module.css';

export default function BookDetailContent({ bookDetail }) {
  const { title, publisher, author, publishedDate, isbn } = bookDetail;

  return (
    <div className={styles.wrapper}>
      
      <h1 className={styles.title}>{title}</h1>
      <div className={styles.detailWrapper}>
        <div className={styles.flexBox}>
          <div>출판</div>
          <div>작가</div>
          <div>출간일</div>
          <div>ISBN</div>
        </div>
        <div className={styles.flexBox}>
          <div>{publisher}</div>
          <div>{author}</div>
          <div>{publishedDate}</div>
          <div>{isbn}</div>
        </div>
      </div>
    </div>
  );
}
