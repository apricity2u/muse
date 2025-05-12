import React from 'react';
import styles from './LoginButton.module.css';

export default function LoginButton({ nickname }) {
  return (
    <button
      className={styles.btn}
      onClick={
        nickname
          ? () => {
              alert('로그아웃');
            }
          : () => {
              alert('로그인');
            }
      }
    >
      {nickname ? '로그아웃' : '로그인'}
    </button>
  );
}
