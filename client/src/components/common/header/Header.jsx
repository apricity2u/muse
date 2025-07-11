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
import basic from '../../../assets/user.png'

export default function Header() {
  const navigate = useNavigate();
  const dispatch = useDispatch();

  const isLoggedIn = useSelector((state) => state.auth.isLoggedIn);
  const nickname = useSelector((state) => state.auth.nickname);
  const userImageUrl = useSelector((state) => state.auth.imageUrl);
  const memberId = useSelector((state) => state.auth.memberId);

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

  const clickProfileHandler = () => {
    navigate(`/users/${memberId}`);
  };

  const loginHandler = () => {
    navigate('/login');
  };

  const logoutHandler = async () => {
    try {
      await authApi.logout();
      dispatch(logout());
      alert('로그아웃 되었습니다.')
      navigate('/');
    } catch (error) {
      // TODO 추후 에러 처리 수정
      console.error(error);
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
              <div className={styles.profileWrapper}>
                <img
                  src={userImageUrl || basic}
                  alt="userImageUrl"
                  className={styles.userImage}
                  onClick={clickProfileHandler}
                />
                <div>
                  <div className={styles.nickname} onClick={openModalHandler}>
                    {nickname}님
                  </div>
                  {isOpen && (
                    <TabDropBoxButton isOpen={isOpen} setIsOpen={setIsOpen}></TabDropBoxButton>
                  )}
                </div>
              </div>
              <RoundButton color="black" clickHandler={logoutHandler}>
                로그아웃
              </RoundButton>
            </div>
          )}
        </div>
      </div>
    </>
  );
}
