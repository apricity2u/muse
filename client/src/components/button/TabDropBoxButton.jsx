import React from 'react';
import styles from './TabDropBoxButton.module.css';

export default function TabDropBoxButton() {
  return (
    <div className={styles.wrapper}>
      <div className={styles.btn} onClick={() => alert('마이페이지')}>
        <img src="user.svg" alt="user" />
        마이페이지
      </div>
      <div className={styles.btn} onClick={() => alert('좋아요')}>
        <img src="/heart.svg" alt="heart" />
        좋아요
      </div>
      <div className={styles.btn} onClick={() => alert('회원정보 수정')}>
        <img src="/notebook.svg" alt="notebook" />
        회원정보 수정
      </div>
    </div>
  );
}
