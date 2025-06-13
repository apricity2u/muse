import { useState } from 'react';
import styles from './SubTabButton.module.css';

export default function SubTabButton({ content1, content2, setIsReview, setSelected }) {
  const tabs = [content1, content2];
  const [selectedIndex, setSelectedIndex] = useState(0);

  const clickHandler = (idx) => {
    setSelectedIndex(idx);
    setIsReview(!idx ? true : false);
    setSelected('createdAt');
  };

  return (
    <div className={styles.tabsContainer}>
      <div className={styles.tabs}>
        {tabs.map((tab, idx) => (
          <div
            key={tab}
            className={`${styles.tab} ${idx === selectedIndex ? styles.active : ''}`}
            onClick={() => clickHandler(idx)}
          >
            {tab}
          </div>
        ))}
      </div>
      <div
        className={styles.underline}
        style={{
          left: `${selectedIndex * 50}%`,
          width: '50%',
        }}
      />
    </div>
  );
}
