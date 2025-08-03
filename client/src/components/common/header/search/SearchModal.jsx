import { useEffect, useRef } from 'react';
import styles from './SearchModal.module.css';
import SearchBoard from '../../board/SearchBoard';
import closeButton from '../../../../assets/icons/reset.png';
import { useNavigate } from 'react-router-dom';

export default function SearchModal({ isSearching, setIsSearching, searchHandler }) {
  const navigate = useNavigate();
  const hasPushedState = useRef(false);

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
    if (isSearching && !hasPushedState.current) {
      window.history.pushState({ modal: true }, '');
      hasPushedState.current = true;
    }

    if (!isSearching) {
      hasPushedState.current = false;
    }

    const escHandler = (event) => {
      if (event.key === 'Escape') {
        setIsSearching(false);
      }
    };

    const popStateHandler = () => {
      if (isSearching) {
        setIsSearching(false);
      }
    };

    if (isSearching) {
      window.addEventListener('keydown', escHandler);
      window.addEventListener('popstate', popStateHandler);
    }

    return () => {
      window.removeEventListener('keydown', escHandler);
      window.removeEventListener('popstate', popStateHandler);

      if (window.history.state && window.history.state.modal) {
        window.history.back();
      }
    };
  }, [isSearching]);

  const clickHandler = (e) => {
    navigate(`/books/${e.target.id}`);
    setIsSearching(false);
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
