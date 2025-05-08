import React from 'react';
import styles from './AlertButton.module.css';

export default function AlertButton({ content, clickHandler }) {
  return (
    <button onClick={clickHandler} className={styles.button}>
      {content}
    </button>
  );
}
