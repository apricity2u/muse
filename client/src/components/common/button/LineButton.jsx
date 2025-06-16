import React from 'react';
import styles from './LineButton.module.css';
import rightIcon from '../../../assets/icons/right.png';

export default function LineButton({ children, isClickable }) {
  return (
    <button className={`${styles.lineButton} ${isClickable ? styles.clickable : ''}`}>
      <span>{children}</span>
      {isClickable && <img src={rightIcon} alt="오른쪽 화살표 아이콘" />}
    </button>
  );
}
