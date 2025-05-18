import React from 'react';
import styles from './PagiNationButton.module.css';
import left from '../../assets/left.png';
import right from '../../assets/right.png';

export default function PageNationButton({ clickHandler, side = 'right' }) {
  const icons = { left: left, right: right };

  return (
    <div className={styles.wrapper}>
      <button className={styles.circle} onClick={clickHandler}>
        <img src={icons[side]} alt={side} />
      </button>
    </div>
  );
}
