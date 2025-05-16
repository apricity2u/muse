import React from 'react';
import styles from './BookCardLists.module.css';
import BookCard from '../card/BookCard';

export default function BookCardLists({ bookCardLists }) {
  return (
    <div className={styles.wrapper}>
      {bookCardLists?.map((bookDetail) => {
        return <BookCard bookDetail={bookDetail}></BookCard>;
      })}
    </div>
  );
}
