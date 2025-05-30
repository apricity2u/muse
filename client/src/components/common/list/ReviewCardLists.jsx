import styles from './ReviewCardLists.module.css';
import ReviewCard from '../card/ReviewCard';

export default function ReviewCardLists({ reviewCardLists, type = 'regular' }) {
  // TODO: 리뷰가 없는 경우 예외처리
  return (
    <div className={styles[type]}>
      {reviewCardLists?.map((reviewDetail) => {
        const { review, book, user } = reviewDetail;
        return (
          <ReviewCard key={review.reviewId} review={review} book={book} user={user}></ReviewCard>
        );
      })}
    </div>
  );
}
