import React from 'react'
import styles from './DropBoxButton.module.css';

export default function DropBoxButton() {
  return (
    <div className={styles.wrapper}>
      <div className={styles.btn} onClick={() => alert('수정')}>수정</div>
      <div className={styles.btn} onClick={() => alert('삭제')}>삭제</div>
    </div>
  )
}
