import React from 'react';
import styles from './AlignButton.module.css';

/**
 * @param clickHandler1 최신순 눌렀을 때 동작할 이벤트
 * @param clickHandler2 좋아요순 눌렀을 때 동작할 이벤트
 * @param selected 선택된 버튼을 결정하는 state
 */
export default function AlignButton({ clickHandler1, clickHandler2, selected = '최신순' }) {
  return (
    <>
      <button
        onClick={clickHandler1}
        className={selected === '최신순' ? styles.selected : styles.button}
      >
        최신순
      </button>

      <button
        onClick={clickHandler2}
        className={selected === '좋아요순' ? styles.selected : styles.button}
      >
        좋아요순
      </button>
    </>
  );
}
