import styles from './BookDetail.module.css';
import React from 'react';
import SubTabButton from '../components/common/button/SubTabButton';

export default function BookDetail() {
  return (
    <div style={styles.wrapper}>
      <div className={styles.bookContainer}></div>
      <div className={styles.subContainer}>
        <SubTabButton />
      </div>
    </div>
  );
}
