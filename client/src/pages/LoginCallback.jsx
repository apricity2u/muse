// src/pages/LoginCallback.jsx
import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import api from '../api/axios';

export default function LoginCallback() {
  const navigate = useNavigate();

  useEffect(() => {

    axios
      .get('http://localhost:8080/api/member/me', {
        withCredentials: true,
      })
      .then((res) => {
        console.log('내 정보:', res.data);

        // TODO: 필요하다면 전역 상태관리(store, context, redux 등)에 유저 정보를 저장

        // user 정보가 정상적으로 조회되면, 홈 페이지나 대시보드로 이동
        navigate('/');
      })
      .catch((err) => {
        console.error('내 정보 조회 실패:', err);
        // 에러가 나면 로그인 페이지로 다시 돌려보내거나 에러 화면 처리
        navigate('/login');
      });
  }, [navigate]);

  return (
    <div style={{ textAlign: 'center', marginTop: '2rem' }}>
      <h2>로그인 처리 중입니다...</h2>
      <p>잠시만 기다려 주세요.</p>
    </div>
  );
}
