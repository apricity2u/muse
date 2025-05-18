import React from 'react';
import ReactDOM from 'react-dom';
import styles from './AlertModal.module.css';
import AlertButton from '../button/AlertButton';

/**
 * @param content 모달창 내용
 * @param clickHandler1 취소 버튼을 눌렀을 때 동작할 이벤트
 * @param clickHandler2 확인 버튼을 눌렀을 때 동작할 이벤트
 */
export default function AlertModal({ children, clickHandler1, clickHandler2 }) {

  return ReactDOM.createPortal(
    <div className={styles.wrapper}>
      <div className={styles.modal}>
        <div className={styles.content}>{children}</div>
        <div>
          <AlertButton color="gray" clickHandler={clickHandler1}>
            취소
          </AlertButton>
          <AlertButton color="blue" clickHandler={clickHandler2}>
            확인
          </AlertButton>
        </div>
      </div>
    </div>,
    document.getElementById('modal-root')
  );
}