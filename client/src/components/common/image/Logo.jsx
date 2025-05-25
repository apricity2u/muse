import styles from './Logo.module.css';
import { useNavigate } from 'react-router-dom';

export default function Logo() {
  const navigate = useNavigate();

  const clickHandler = () => {
    navigate('/');
  };

  return <img src="/logo.png" alt="logo" className={styles.logo} onClick={clickHandler} />;
}
