import { useEffect, useState } from 'react';
import styles from './Login.module.css';
import kakao from '../assets/login-kakao.png';
import naver from '../assets/login-naver.png';
import google from '../assets/login-google.png';
import { useNavigate } from 'react-router-dom';

export default function Login() {
  const KAKAO_AUTH_URL = `${import.meta.env.VITE_API_URL}/oauth2/authorization/kakao`;
  const NAVER_AUTH_URL = `${import.meta.env.VITE_API_URL}/oauth2/authorization/naver`;
  const GOOGLE_AUTH_URL = `${import.meta.env.VITE_API_URL}/oauth2/authorization/google`;
  const DEMO_AUTH_URL = `${import.meta.env.VITE_API_URL}/auth/demoLogin`;

  const [lastProvider, setLastProvider] = useState(null);

  const navigate = useNavigate();

  useEffect(() => {
    const savedProvider = localStorage.getItem('lastLoginProvider');
    setLastProvider(savedProvider);
  }, []);

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
        onClick={() => {
          localStorage.setItem('lastLoginProvider', 'kakao');
          window.location.href = KAKAO_AUTH_URL;
        }}
      >
        <img src={kakao} alt="kakaoLogin" className={styles.loginButtonImage} />
        {lastProvider === 'kakao' && (
          <div className={styles.highlighted} onClick={(e) => e.stopPropagation()}>
            최근 로그인
          </div>
        )}
      </button>
      <button
        className={styles.loginButton}
        onClick={() => {
          localStorage.setItem('lastLoginProvider', 'naver');
          window.location.href = NAVER_AUTH_URL;
        }}
      >
        <img src={naver} alt="naverLogin" className={styles.loginButtonImage} />
        {lastProvider === 'naver' && (
          <div className={styles.highlighted} onClick={(e) => e.stopPropagation()}>
            최근 로그인
          </div>
        )}
      </button>
      <button
        className={styles.loginButton}
        onClick={() => {
          localStorage.setItem('lastLoginProvider', 'google');
          window.location.href = GOOGLE_AUTH_URL;
        }}
      >
        <img src={google} alt="googleLogin" className={styles.loginButtonImage} />
        {lastProvider === 'google' && (
          <div className={styles.highlighted} onClick={(e) => e.stopPropagation()}>
            최근 로그인
          </div>
        )}
      </button>
      <button className={styles.demoLoginButton} onClick={() => {
        localStorage.setItem('lastLoginProvider', 'demo');
        window.location.href = DEMO_AUTH_URL;
      }}>
        데모용 로그인
        {lastProvider === 'demo' && (
          <div className={styles.highlighted} onClick={(e) => e.stopPropagation()}>
            최근 로그인
          </div>
        )}
      </button>
    </div>
  );
}
