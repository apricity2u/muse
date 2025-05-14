import React, { useState } from 'react';
import styles from './Header.module.css';
import Logo from '../Logo';
import TabButton from '../button/TabButton';
import RoundButton from '../button/RoundButton';
import { useNavigate } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import authApi from '../../api/authApi';
import { logout } from '../../store/slices/authSlice';

export default function Header() {
  const navigate = useNavigate();
  const dispatch = useDispatch();

  const isLoggedIn = useSelector((state) => state.auth.isLoggIn);
  const nickname = useSelector((state) => state.auth.nickname);

  const [isSearching, setIsSearching] = useState(false);

  const writeHandler = () => {
    navigate('/reviews/create');
  };

  const searchHandler = () => {
    setIsSearching(!isSearching);
  };

  const loginHandler = () => {
    navigate('/login');
  };

  const logoutHandler = async () => {
    await authApi.logout();
    dispatch(logout());
  };

  return (
    <div className={styles.wrapper}>
      <div className={styles.header}>
        <div className={styles.flexBox}>
          <Logo></Logo>
          <TabButton clickHandler={writeHandler}>글쓰기</TabButton>
          <TabButton clickHandler={searchHandler}>검색</TabButton>
        </div>
        {!isLoggedIn ? (
          <RoundButton clickHandler={loginHandler}>로그인</RoundButton>
        ) : (
          <div className={styles.flexBox}>
            <div className={styles.nickname}>{nickname}님</div>
            <RoundButton color="black" clickHandler={logoutHandler}>
              로그아웃
            </RoundButton>
          </div>
        )}
      </div>
      <hr className={styles.line} />
    </div>
  );
}
