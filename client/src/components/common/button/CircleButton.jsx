import styles from './CircleButton.module.css';
import edit from '../../../assets/icons/edit.svg';
import link from '../../../assets/icons/link.svg';
import heart from '../../../assets/icons/heart.png';
import heartFilled from '../../../assets/icons/heart_filled.png';

export default function CircleButton({ clickHandler, children = '좋아요', isLike }) {
  const icons = { '리뷰 작성': edit, '링크 복사': link, 좋아요: !isLike ? heart : heartFilled };

  return (
    <div className={styles.wrapper}>
      <button className={styles.circle} onClick={clickHandler}>
        <img src={icons[children]} alt={children} />
        <div className={styles.tooltip}>{children}</div>
      </button>
    </div>
  );
}
