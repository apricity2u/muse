import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import styles from './ReviewCard.module.css';
import CardFront from './review_card/CardFront';
import CardBack from './review_card/CardBack';

export default function ReviewCard({ reviewDetail, setReviewCount, size = 'small' }) {
  const { review, book, user } = reviewDetail;
  const navigate = useNavigate();

  const [toggleCard, setToggleCard] = useState(false);
  const [isOpen, setIsOpen] = useState(false);
  const [isDelete, setIsDelete] = useState(false);

  const toggleCardHandler = () => {
    setToggleCard(!toggleCard);
  };

  const clickProfileHandler = () => {
    navigate(`/users/${user.memberId}`);
  };

  return (
    <div className={`${styles[size]} ${isDelete && styles.hide} ${isOpen && styles.zIndex}`}>
      <div className={`${styles.card} ${styles[size]} ${toggleCard && styles.flipped}`}>
        <CardFront
          review={review}
          user={user}
          bookId={book.id}
          toggleCard={toggleCard}
          setReviewCount={setReviewCount}
          isDelete={isDelete}
          setIsDelete={setIsDelete}
          isOpen={isOpen}
          setIsOpen={setIsOpen}
          toggleCardHandler={toggleCardHandler}
          clickProfileHandler={clickProfileHandler}
        ></CardFront>
        <CardBack
          book={book}
          user={user}
          toggleCard={toggleCard}
          toggleCardHandler={toggleCardHandler}
          clickProfileHandler={clickProfileHandler}
        ></CardBack>
      </div>
    </div>
  );
}
