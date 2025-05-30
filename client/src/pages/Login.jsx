import React from 'react';
import styles from './Login.module.css';
import kakao from '../assets/login-kakao.png';
import naver from '../assets/login-naver.png';
import google from '../assets/login-google.png';
import authApi from '../api/authApi';

export default function Login() {
  return (
    <div className={styles.wrapper}>
      <img src="logo.png" alt="muse" style={{ width: '10%' }} className={styles.logo} />
      <p className={styles.description}>책의 순간을 한 줄로 기록하는 공간</p>
      <button className={styles.loginButton} onClick={authApi.print}>
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
