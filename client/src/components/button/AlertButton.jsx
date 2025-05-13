import React from 'react';
import clsx from 'clsx';
import styles from './AlertButton.module.css';

export default function AlertButton({ children, clickHandler, color = 'blue' }) {
  return (
    <button onClick={clickHandler} className={clsx(styles.button, styles[color])}>
      {children}
    </button>
  );
}
