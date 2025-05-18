import React from 'react';
import styles from './BookCardLists.module.css';
import BookCard from '../card/BookCard';

export default function BookCardLists({ bookCardLists }) {
  // TODO: 도서가 없는 경우 처리
  return (
    <div className={styles.wrapper}>
      {bookCardLists?.map((bookDetail) => {
        return <BookCard key={bookDetail.id} bookDetail={bookDetail}></BookCard>;
      })}
    </div>
  );
}
