import { useEffect, useRef, useState } from 'react';
import SearchResultItem from '../item/SearchResultItem';
import styles from './SearchBoard.module.css';
import search from '../../../assets/icons/search.png';
import reset from '../../../assets/icons/reset.png';
import bookApi from '../../../api/bookApi';

export default function SearchBoard({ clickHandler }) {
  const [inputTitle, setInputTitle] = useState('');
  const [searchKeyword, setSearchKeyword] = useState('');
  const [bookList, setBookList] = useState([]);
  const [focusedIndex, setFocusedIndex] = useState(-1);
  const listRef = useRef([]);
  const isComposing = useRef(false);

  useEffect(() => {
    const searchBooksHandler = async () => {
      if (!inputTitle.trim()) {
        setBookList([]);
        return;
      }

      try {
        const response = await bookApi.searchTitle(inputTitle.trim());
        const data = response.data.data;
        setFocusedIndex(-1);
        setBookList(data);
      } catch (error) {
        alert('도서 검색 중 문제가 발생했습니다. 잠시 후 다시 시도해주세요.');
      }
    };
    searchBooksHandler();
  }, [inputTitle]);

  const handleCompositionStart = () => {
    isComposing.current = true;
  };

  const handleCompositionEnd = (e) => {
    isComposing.current = false;
    setSearchKeyword(e.target.value);
  };

  const keyDownHandler = (e) => {
    if (e.key === 'ArrowDown') {
      e.preventDefault();
      setFocusedIndex((prev) => Math.min(prev + 1, bookList.length - 1));
    } else if (e.key === 'ArrowUp') {
      e.preventDefault();
      setFocusedIndex((prev) => Math.max(prev - 1, 0));
    } else if (e.key === 'Enter') {
      if (focusedIndex >= 0 && bookList[focusedIndex]) {
        clickHandler({ target: { id: bookList[focusedIndex].id } });
      }
    }
  };

  const inputHandler = (e) => {
    const newValue = e.target.value;
    setInputTitle(newValue);

    if (!isComposing.current) {
      setSearchKeyword(newValue);
    }
  };

  const clickResetHandler = () => {
    setInputTitle('');
    setSearchKeyword('');
    setBookList([]);
    setFocusedIndex(-1);
  };

  return (
    <div className={styles.wrapper}>
      <div>
        <div className={styles.searchBar}>
          <img src={search} alt="search" className={styles.icon} />
          <input
            type="text"
            placeholder="도서명 검색"
            value={inputTitle}
            onChange={inputHandler}
            onKeyDown={keyDownHandler}
            onCompositionStart={handleCompositionStart}
            onCompositionEnd={handleCompositionEnd}
          />
          <img src={reset} alt="reset" className={styles.resetButton} onClick={clickResetHandler} />
        </div>
        <hr className={styles.underLine} />
      </div>
      <ul>
        {bookList.length > 0 ? (
          <>
            {bookList.slice(0, 10).map((book, index) => {
              const { id, title } = book;
              return (
                <SearchResultItem
                  key={id}
                  id={id}
                  title={title}
                  keyword={searchKeyword}
                  clickHandler={clickHandler}
                  isFocused={index === focusedIndex}
                  ref={(el) => (listRef.current[index] = el)}
                />
              );
            })}
          </>
        ) : (
          inputTitle && <div className={styles.noneResultMessage}>검색 결과가 없습니다.</div>
        )}
      </ul>
    </div>
  );
}
