import React, { useEffect } from 'react';
import styles from './SearchModal.module.css';
import SearchBoard from '../../components/board/SearchBoard';
import closeButton from '../../assets/reset.png';
import { useNavigate } from 'react-router-dom';

export default function SearchModal({ isSearching, setIsSearching, searchHandler }) {
  const navigate = useNavigate();

  useEffect(() => {
    if (isSearching) {
      document.body.style.overflow = 'hidden';
    } else {
      document.body.style.overflow = 'auto';
    }

    return () => {
      document.body.style.overflow = 'auto';
    };
  }, [isSearching]);

  useEffect(() => {
    const escHandler = (event) => {
      if (event.key === 'Escape') {
        setIsSearching(false);
      }
    };

    if (isSearching) {
      window.addEventListener('keydown', escHandler);
    }

    return () => {
      window.removeEventListener('keydown', escHandler);
    };
  }, [isSearching]);

  const clickHandler = (e) => {
    navigate(`/books/${e.target.id}`);
    setIsSearching(!isSearching);
  };

  return (
    <div className={styles.modal}>
      <div className={styles.closeButtonWrapper}>
        <img
          src={closeButton}
          alt="closeButton"
          className={styles.closeButton}
          onClick={searchHandler}
        />
      </div>
      <div className={styles.searchBoardWrapper}>
        <SearchBoard clickHandler={clickHandler}></SearchBoard>
      </div>
    </div>
  );
}
