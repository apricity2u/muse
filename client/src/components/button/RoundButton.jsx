import React from 'react';
import styles from './RoundButton.module.css';

export default function RoundButton({content}) {
  return <button className={styles.btn}>{content}</button>;
}
