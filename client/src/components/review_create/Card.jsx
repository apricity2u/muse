import styles from './Card.module.css';
import { useSelector } from 'react-redux';

export default function Card({ review }) {
  const { imageUrl, content } = review;
  const nickname = useSelector((state) => state.auth.nickname);
  const userImageUrl = useSelector((state) => state.auth.imageUrl);

  return (
    <div className={styles.card}>
      <div className={styles.topWrapper}>
        <div className={styles.imageWrapper}>
          <img src={imageUrl} alt="reviewImage" />
        </div>
        <div className={styles.content}>{content}</div>
      </div>
      <hr />
      <div className={styles.bottomWrapper}>
        <div className={styles.flexBox}>
          <div className={styles.profileImageWrapper}>
            <img src={userImageUrl} alt="profileImage" />
          </div>
          <div className={styles.nickname}>{nickname}</div>
        </div>
      </div>
    </div>
  );
}
