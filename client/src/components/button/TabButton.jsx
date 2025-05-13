import React from 'react';
import { useNavigate } from 'react-router-dom';
import styles from './TabButton.module.css';

export default function TabButton({ content }) {
  const navigate = useNavigate();

  return <div onClick={() => alert(content)} className={styles.btn}>{content}</div>;
}
