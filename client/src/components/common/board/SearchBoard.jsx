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
      try {
        const response = await bookApi.searchTitle(inputTitle);
        const data = response.data.data;

        setBookList(data);
      } catch (error) {
        // TODO: api 연결 후 error 부분 수정
        console.log(error);
      }
    };
    searchBooksHandler();
  }, [inputTitle]);

  const inputHandler = (e) => {
    setInputTitle(e.target.value);
  };

  const clickResetHandler = () => {
    setInputTitle('');
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
        {bookList?.slice(0, 10).map((book) => {
          const { id, title } = book;
          return (
            <SearchResultItem
              key={id}
              id={id}
              title={title}
              clickHandler={clickHandler}
            ></SearchResultItem>
          );
        })}
      </ul>
    </div>
  );
}
