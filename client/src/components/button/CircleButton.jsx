import React from 'react';
import styles from './CircleButton.module.css';

export default function CircleButton({ clickHandler, content = '좋아요' }) {
  const icons = { '리뷰 작성': '/edit.svg', '링크 복사': '/link.svg', 좋아요: '/heart.svg' };

  return (
    <div className={styles.wrapper}>
      <button className={styles.circle} onClick={clickHandler}>
        <img src={icons[content]} alt={content} />
        <div className={styles.tooltip}>{content}</div>
      </button>
    </div>
  );
}
