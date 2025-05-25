import styles from './PagiNationButton.module.css';
import left from '../../../assets/icons/left.png';
import right from '../../../assets/icons/right.png';

export default function PaginationButton({ clickHandler, side = 'right' }) {
  const icons = { left: left, right: right };

  return (
    <div className={styles.wrapper}>
      <button className={styles.circle} onClick={clickHandler}>
        <img src={icons[side]} alt={side} />
      </button>
    </div>
  );
}
