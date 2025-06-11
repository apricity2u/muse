import React from 'react';
import styles from './Login.module.css';
import kakao from '../assets/login-kakao.png';
import naver from '../assets/login-naver.png';
import google from '../assets/login-google.png';
import authApi from '../api/authApi';
import { useNavigate } from 'react-router-dom';

export default function Login() {
  const KAKAO_AUTH_URL = `${import.meta.env.VITE_API_URL}/oauth2/authorization/kakao`;
  const NAVER_AUTH_URL = `${import.meta.env.VITE_API_URL}/oauth2/authorization/naver`;
  const GOOGLE_AUTH_URL = `${import.meta.env.VITE_API_URL}/oauth2/authorization/google`;

  const navigate = useNavigate();

  return (
    <div className={styles.wrapper}>
      <img
        src="logo.png"
        alt="muse"
        style={{ width: '10%' }}
        className={styles.logo}
        onClick={() => navigate('/')}
      />
      <p className={styles.description}>책의 순간을 한 줄로 기록하는 공간</p>
      <button
        className={styles.loginButton}
        onClick={() => (window.location.href = KAKAO_AUTH_URL)}
      >
        <img src={kakao} alt="kakaoLogin" className={styles.loginButtonImage} />
      </button>
      <button
        className={styles.loginButton}
        onClick={() => (window.location.href = NAVER_AUTH_URL)}
      >
        <img src={naver} alt="naverLogin" className={styles.loginButtonImage} />
      </button>
      <button
        className={styles.loginButton}
        onClick={() => (window.location.href = GOOGLE_AUTH_URL)}
      >
        <img src={google} alt="googleLogin" className={styles.loginButtonImage} />
      </button>
    </div>
  );
}
