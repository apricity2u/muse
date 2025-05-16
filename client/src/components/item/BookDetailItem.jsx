import React from 'react';
import styles from './BookDetailItem.module.css'
import CircleButton from '../button/CircleButton';

export default function BookDetailItem() {
  return (
    <div className={styles.wrapper}>
      <CircleButton>리뷰 작성</CircleButton>
      <CircleButton>링크 복사</CircleButton>
      <CircleButton>좋아요</CircleButton>
    </div>
  );
}
