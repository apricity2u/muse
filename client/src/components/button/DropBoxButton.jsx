import React from 'react';
import styles from './DropBoxButton.module.css';

export default function DropBoxButton({ clickHandler1, clickHandler2 }) {
  return (
    <div className={styles.wrapper}>
      <div className={styles.btn} onClick={clickHandler1}>
        수정
      </div>
      <div className={styles.btn} onClick={clickHandler2}>
        삭제
      </div>
    </div>
  );
}
