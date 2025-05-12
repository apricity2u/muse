import React from 'react';
import styles from './AlertModal.module.css';
import AlertButton from '../button/AlertButton';

export default function AlertModal({ content, clickHandler1, clickHandler2 }) {
  return (
    <div className={styles.modal}>
      <div className={styles.content}>{'리뷰를 삭제하시겠습니까?'}</div>
      <div>
        <AlertButton color="gray" clickHandler={clickHandler1}>
          취소
        </AlertButton>
        <AlertButton color="blue" clickHandler={clickHandler2}>
          확인
        </AlertButton>
      </div>
    </div>
  );
}
