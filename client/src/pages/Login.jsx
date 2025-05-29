import React from 'react';
import styles from './Login.module.css';
import kakao from '../assets/login-kakao.png';
import naver from '../assets/login-naver.png';
import google from '../assets/login-google.png';

export default function Login() {
  return (
    <div className={styles.wrapper}>
      <img src="logo.png" alt="muse" style={{ width: '10%' }} />
      <button className={styles.loginButton}>
        <img src={kakao} alt="kakaoLogin" className={styles.loginButtonImage} />
      </button>
      <button className={styles.loginButton}>
        <img src={naver} alt="naverLogin" className={styles.loginButtonImage} />
      </button>
      <button className={styles.loginButton}>
        <img src={google} alt="googleLogin" className={styles.loginButtonImage} />
      </button>
    </div>
  );
}
