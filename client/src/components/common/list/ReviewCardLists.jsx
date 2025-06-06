import styles from './ReviewCardLists.module.css';
import ReviewCard from '../card/ReviewCard';

export default function ReviewCardLists({ reviewCardLists, type = 'regular', size = 'small' }) {
  // TODO: 리뷰가 없는 경우 예외처리
  return (
    <div className={styles[type]}>
      {reviewCardLists?.map((reviewDetail) => {
        const { review } = reviewDetail;
        return <ReviewCard key={review.id} reviewDetail={reviewDetail} size={size}></ReviewCard>;
      })}
    </div>
  );
}
