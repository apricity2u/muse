import React from 'react';
import styles from './AlertModal.module.css';
import AlertButton from '../button/AlertButton';

/**
  * @param content 모달창 내용
  * @param clickHandler1 취소 버튼을 눌렀을 때 동작할 이벤트
  * @param clickHandler2 확인 버튼을 눌렀을 때 동작할 이벤트 
*/
export default function AlertModal({ content, clickHandler1, clickHandler2 }) {
  return (
    <div className={styles.modal}>
      <div className={styles.content}>{content}</div>
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
