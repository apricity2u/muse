// LoginCallback.jsx
import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../api/axios';
import { useDispatch } from 'react-redux';
import { login, logout } from '../store/slices/authSlice';
import store from '../store/store';

export default function LoginCallback() {
  const navigate = useNavigate();
  const dispatch = useDispatch();

  useEffect(() => {
    const fetchLoginSuccess = async () => {
      try {
        console.log('LoginCallback: /auth/success 호출 전');

        const response = await api.get('/auth/success');  

        const data = response.data.data || {};
        const nickname = data?.nickname || '';
        const imageUrl = data?.imageUrl || '';

        dispatch(
          login({
            accessToken: store.getState().auth.accessToken,
            nickname,
            imageUrl,
          })
        );

        console.log('로그인 성공, 메인 페이지로 이동');
        navigate('/');
      } catch (error) {
        console.error('LoginCallback 에러 발생:', error);
        dispatch(logout());
        navigate('/login');
      }
    };

    fetchLoginSuccess();
  }, [dispatch, navigate]);

  return (
    <div style={{ textAlign: 'center', marginTop: '2rem' }}>
      <h2>로그인 처리 중입니다… 잠시만 기다려 주세요.</h2>
    </div>
  );
}
