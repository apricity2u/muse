import React, { useEffect, useState } from 'react';
import SearchResultItem from '../item/SearchResultItem';
import styles from './SearchBoard.module.css';
import search from '../../assets/search.png';
import reset from '../../assets/reset.png';
import bookApi from '../../api/bookApi';

export default function SearchBoard({ clickHandler }) {
  const [inputTitle, setInputTitle] = useState('');
  const [bookList, setBookList] = useState([]);

  const searchBooksHandler = async () => {
    const response = await bookApi.searchTitle(inputTitle);
    const data = response.data.data;

    setBookList(data);
  };

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
          <img src={reset} alt="reset" className={styles.icon} onClick={clickResetHandler} />
        </div>
        <hr className={styles.underLine} />
      </div>
      <ul>
        {bookList.map((book) => {
          const { id, title } = book;
          <SearchResultItem id={id} title={title} clickHandler={clickHandler}></SearchResultItem>;
        })}
      </ul>
    </div>
  );
}
