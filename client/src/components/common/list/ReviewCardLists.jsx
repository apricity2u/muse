import styles from './ReviewCardLists.module.css';
import ReviewCard from '../card/ReviewCard';

export default function ReviewCardLists({
  reviewCardLists,
  setUserInfo,
  type = 'regular',
  size = 'small',
}) {
  // TODO: 리뷰가 없는 경우 예외처리
  return (
    <div className={styles[type]}>
      {reviewCardLists?.map((reviewDetail) => {
        const { review } = reviewDetail;
        return (
          <div key={review.id}>
            <ReviewCard
              reviewDetail={reviewDetail}
              setUserInfo={setUserInfo}
              size={size}
            ></ReviewCard>
          </div>
        );
      })}
    </div>
  );
}
