import styles from './TabDropBoxButton.module.css';
import { useNavigate } from 'react-router-dom';
import { useSelector } from 'react-redux';
import user from '../../../assets/icons/user.svg';
import heart from '../../../assets/icons/heart.png';
import notebook from '../../../assets/icons/notebook.svg';

export default function TabDropBoxButton({ isOpen, setIsOpen }) {
  const navigate = useNavigate();

  const memberId = useSelector((state) => state.auth.memberId);

  const clickHandler1 = () => {
    setIsOpen(!isOpen);
    navigate(`/users/${memberId}`);
  };

  const clickHandler2 = () => {
    setIsOpen(!isOpen);
    navigate('/likes');
  };

  const clickHandler3 = () => {
    setIsOpen(!isOpen);
    navigate('/profile');
  };

  return (
    <div className={styles.wrapper}>
      <div className={styles.btn} onClick={clickHandler1}>
        <img src={user} alt="user" />
        마이페이지
      </div>
      <div className={styles.btn} onClick={clickHandler2}>
        <img src={heart} alt="heart" />
        좋아요
      </div>
      <div className={styles.btn} onClick={clickHandler3}>
        <img src={notebook} alt="notebook" />
        회원정보 수정
      </div>
    </div>
  );
}
