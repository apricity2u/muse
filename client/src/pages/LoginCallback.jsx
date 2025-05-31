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


        const response = await api.get('/auth/success');

        const data = response.data.data || {};
        const nickname = data?.nickname || '';
        const imageUrl = data?.imageUrl || '';

        dispatch(
          login({
            accessToken: store.getState().auth.accessToken,
            nickname,
            imageUrl,
          }),
        );



        navigate('/');
      } catch (error) {
        console.error('LoginCallback 에러 발생:', error);
        dispatch(logout());
        navigate('/login');
      }
    };

    fetchLoginSuccess();
  }, [dispatch, navigate]);

  return <></>;
}
