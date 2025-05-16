import React from 'react';
import styles from './CircleButton.module.css';
import edit from '../../assets/edit.svg';
import link from '../../assets/link.svg';
import heart from '../../assets/heart.png';

export default function CircleButton({ clickHandler, children = '좋아요' }) {
  const icons = { '리뷰 작성': edit, '링크 복사': link, 좋아요: heart };

  return (
    <div className={styles.wrapper}>
      <button className={styles.circle} onClick={clickHandler}>
        <img src={icons[children]} alt={children} />
        <div className={styles.tooltip}>{children}</div>
      </button>
    </div>
  );
}
