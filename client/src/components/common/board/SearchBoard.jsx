import { useEffect, useState } from 'react';
import SearchResultItem from '../item/SearchResultItem';
import styles from './SearchBoard.module.css';
import search from '../../../assets/icons/search.png';
import reset from '../../../assets/icons/reset.png';
import bookApi from '../../../api/bookApi';

export default function SearchBoard({ clickHandler }) {
  const [inputTitle, setInputTitle] = useState('');
  const [bookList, setBookList] = useState([]);

  useEffect(() => {
    const searchBooksHandler = async () => {
      if (!inputTitle.trim()) {
        setBookList([]);
        return;
      }

      try {
        const response = await bookApi.searchTitle(inputTitle.trim());
        const data = response.data;
        setBookList(data);
      } catch (error) {
        alert('도서 검색 중 문제가 발생했습니다. 잠시 후 다시 시도해주세요.');
      }
    };
    searchBooksHandler();
  }, [inputTitle]);

  const inputHandler = (e) => {
    setInputTitle(e.target.value);
  };

  const clickResetHandler = () => {
    setInputTitle('');
    setBookList([]);
  };

  return (
    <div className={styles.wrapper}>
      <div>
        <div className={styles.searchBar}>
          <img src={search} alt="search" className={styles.icon} />
          <input type="text" placeholder="도서명 검색" value={inputTitle} onChange={inputHandler} />
          <img src={reset} alt="reset" className={styles.resetButton} onClick={clickResetHandler} />
        </div>
        <hr className={styles.underLine} />
      </div>
      <ul>
        {bookList ? (
          <>
            {bookList.slice(0, 10).map((book) => {
              const { id, title } = book;
              return (
                <SearchResultItem
                  key={id}
                  id={id}
                  title={title}
                  keyword={inputTitle}
                  clickHandler={clickHandler}
                ></SearchResultItem>
              );
            })}
          </>
        ) : (
          <div>검색 결과가 없습니다.</div>
        )}
      </ul>
    </div>
  );
}
