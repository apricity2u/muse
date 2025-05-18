import React from 'react';
import styles from './ReviewCardLists.module.css';
import ReviewCard from '../card/ReviewCard';

export default function ReviewCardLists({ reviewCardLists  }) {
  return (
    <div className={styles.wrapper}>
      {reviewCardLists?.map((reviewDetail) => {
        const { review, book, user } = reviewDetail;
        return <ReviewCard review={review} book={book} user={user}></ReviewCard>;
      })}
    </div>
  );
}
