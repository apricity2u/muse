import { useNavigate } from 'react-router-dom';
import RoundButton from './common/button/RoundButton';
import styles from './NotFound.module.css';

export default function NotFound() {
  const navigate = useNavigate();

  const clickHandler = () => {
    navigate('/');
  };

  return (
    <div className={styles.container}>
      <div className={styles.wrapper}>
        <div className={styles.flexbox}>
          <div className={styles.number}>404</div>
          <div className={styles.notFound}>NotFound</div>
        </div>
        <RoundButton clickHandler={clickHandler}>메인화면으로 이동</RoundButton>
      </div>
    </div>
  );
}
