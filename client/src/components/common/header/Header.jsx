import { useState } from 'react';
import styles from './Header.module.css';
import Logo from '../image/Logo';
import TabButton from '../button/TabButton';
import RoundButton from '../button/RoundButton';
import TabDropBoxButton from '../button/TabDropBoxButton';
import { useNavigate } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import { logout } from '../../../store/slices/authSlice';
import authApi from '../../../api/authApi';
import SearchModal from './search/SearchModal';

export default function Header() {
  const navigate = useNavigate();
  const dispatch = useDispatch();

  const isLoggedIn = useSelector((state) => state.auth.isLoggIn);
  const nickname = useSelector((state) => state.auth.nickname);

  const [isSearching, setIsSearching] = useState(false);
  const [isOpen, setIsOpen] = useState(false);

  const writeHandler = () => {
    navigate('/reviews/create');
  };

  const searchHandler = () => {
    setIsSearching(!isSearching);
  };

  const openModalHandler = () => {
    setIsOpen(!isOpen);
  };

  const loginHandler = () => {
    navigate('/login');
  };

  const logoutHandler = async () => {
    try {
      await authApi.logout();
      dispatch(logout());
      navigate('/');
    } catch (error) {
      // TODO 추후 에러 처리 수정
      console.error('로그아웃에 실패했습니다!');
      console.log(error);
    }
  };

  return (
    <>
      {isSearching && (
        <SearchModal
          isSearching={isSearching}
          setIsSearching={setIsSearching}
          searchHandler={searchHandler}
        ></SearchModal>
      )}
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
              <div>
                <div className={styles.nickname} onClick={openModalHandler}>
                  {nickname}님
                </div>
                {isOpen && (
                  <TabDropBoxButton isOpen={isOpen} setIsOpen={setIsOpen}></TabDropBoxButton>
                )}
              </div>
              <RoundButton color="black" clickHandler={logoutHandler}>
                로그아웃
              </RoundButton>
            </div>
          )}
        </div>
        <hr className={styles.line} />
      </div>
    </>
  );
}
