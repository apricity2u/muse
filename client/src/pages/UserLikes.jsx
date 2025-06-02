import styles from './UserLikes.module.css';
import ReviewCardLists from '../components/common/list/ReviewCardLists';
import SubTabButton from '../components/common/button/SubTabButton';
import AlignButton from '../components/common/button/AlignButton';
import dog from '../assets/dog.jpg';
import harry from '../assets/harry.jpg';
import { useSelector } from 'react-redux';

export default function UserLikes() {
  const reviewCardLists = [
    {
      review: {
        reviewId: 1,
        reviewImageUrl: dog,
        content: '내용1',
        reviewLikes: 12,
        reviewIsLike: false,
      },

      book: {
        bookId: 1,
        bookImageUrl: harry,
        title: '해리포터1',
        author: '작가1',
        publisher: '출판사1',
        bookLikes: 34,
        bookIsLike: false,
      },

      user: {
        userId: 1,
        nickname: '삼상호',
        userImageUrl: dog,
      },
    },
    {
      review: {
        reviewId: 2,
        reviewImageUrl: dog,
        content: '내용2',
        reviewLikes: 56,
        reviewIsLike: false,
      },

      book: {
        bookId: 2,
        bookImageUrl: harry,
        title: '해리포터2',
        author: '작가1',
        publisher: '출판사2',
        bookLikes: 78,
        bookIsLike: false,
      },

      user: {
        userId: 2,
        nickname: '사상호',
        userImageUrl: dog,
      },
    },
    {
      review: {
        reviewId: 3,
        reviewImageUrl: dog,
        content: '내용3',
        reviewLikes: 90,
        reviewIsLike: false,
      },

      book: {
        bookId: 3,
        bookImageUrl: harry,
        title: '해리포터3',
        author: '작가1',
        publisher: '출판사3',
        bookLikes: 11,
        bookIsLike: false,
      },

      user: {
        userId: 3,
        nickname: '오상호',
        userImageUrl: dog,
      },
    },
    {
      review: {
        reviewId: 3,
        reviewImageUrl: dog,
        content: '내용3',
        reviewLikes: 90,
        reviewIsLike: false,
      },

      book: {
        bookId: 3,
        bookImageUrl: harry,
        title: '해리포터3 해리포터3 해리포터3 해리포터3',
        author: '작가1',
        publisher: '출판사3',
        bookLikes: 11,
        bookIsLike: false,
      },

      user: {
        userId: 3,
        nickname: '오상호',
        userImageUrl: dog,
      },
    },
    {
      review: {
        reviewId: 1,
        reviewImageUrl: dog,
        content: '내용1',
        reviewLikes: 12,
        reviewIsLike: false,
      },

      book: {
        bookId: 1,
        bookImageUrl: harry,
        title: '해리포터1',
        author: '작가1',
        publisher: '출판사1',
        bookLikes: 34,
        bookIsLike: false,
      },

      user: {
        userId: 1,
        nickname: '삼상호',
        userImageUrl: dog,
      },
    },
    {
      review: {
        reviewId: 2,
        reviewImageUrl: dog,
        content: '내용2',
        reviewLikes: 56,
        reviewIsLike: false,
      },

      book: {
        bookId: 2,
        bookImageUrl: harry,
        title: '해리포터2',
        author: '작가1',
        publisher: '출판사2',
        bookLikes: 78,
        bookIsLike: false,
      },

      user: {
        userId: 2,
        nickname: '사상호',
        userImageUrl: dog,
      },
    },
    {
      review: {
        reviewId: 3,
        reviewImageUrl: dog,
        content: '내용3',
        reviewLikes: 90,
        reviewIsLike: false,
      },

      book: {
        bookId: 3,
        bookImageUrl: harry,
        title: '해리포터3',
        author: '작가1',
        publisher: '출판사3',
        bookLikes: 11,
        bookIsLike: false,
      },

      user: {
        userId: 3,
        nickname: '오상호',
        userImageUrl: dog,
      },
    },
    {
      review: {
        reviewId: 3,
        reviewImageUrl: dog,
        content: '내용3',
        reviewLikes: 90,
        reviewIsLike: false,
      },

      book: {
        bookId: 3,
        bookImageUrl: harry,
        title: '해리포터3 해리포터3 해리포터3 해리포터3',
        author: '작가1',
        publisher: '출판사3',
        bookLikes: 11,
        bookIsLike: false,
      },

      user: {
        userId: 3,
        nickname: '오상호',
        userImageUrl: dog,
      },
    },
    {
      review: {
        reviewId: 1,
        reviewImageUrl: dog,
        content: '내용1',
        reviewLikes: 12,
        reviewIsLike: false,
      },

      book: {
        bookId: 1,
        bookImageUrl: harry,
        title: '해리포터1',
        author: '작가1',
        publisher: '출판사1',
        bookLikes: 34,
        bookIsLike: false,
      },

      user: {
        userId: 1,
        nickname: '삼상호',
        userImageUrl: dog,
      },
    },
    {
      review: {
        reviewId: 2,
        reviewImageUrl: dog,
        content: '내용2',
        reviewLikes: 56,
        reviewIsLike: false,
      },

      book: {
        bookId: 2,
        bookImageUrl: harry,
        title: '해리포터2',
        author: '작가1',
        publisher: '출판사2',
        bookLikes: 78,
        bookIsLike: false,
      },

      user: {
        userId: 2,
        nickname: '사상호',
        userImageUrl: dog,
      },
    },
    {
      review: {
        reviewId: 3,
        reviewImageUrl: dog,
        content: '내용3',
        reviewLikes: 90,
        reviewIsLike: false,
      },

      book: {
        bookId: 3,
        bookImageUrl: harry,
        title: '해리포터3',
        author: '작가1',
        publisher: '출판사3',
        bookLikes: 11,
        bookIsLike: false,
      },

      user: {
        userId: 3,
        nickname: '오상호',
        userImageUrl: dog,
      },
    },
    {
      review: {
        reviewId: 3,
        reviewImageUrl: dog,
        content: '내용3',
        reviewLikes: 90,
        reviewIsLike: false,
      },

      book: {
        bookId: 3,
        bookImageUrl: harry,
        title: '해리포터3 해리포터3 해리포터3 해리포터3',
        author: '작가1',
        publisher: '출판사3',
        bookLikes: 11,
        bookIsLike: false,
      },

      user: {
        userId: 3,
        nickname: '오상호',
        userImageUrl: dog,
      },
    },
  ];

  const user = useSelector((state) => state.auth);
  const { imageUrl, nickname } = user;
  return (
    <div className={styles.container}>
      <div className={styles.wrapper}>
        <div className={styles.profileWrapper}>
          <div className={styles.subTitle}>좋아요 표시한 컨텐츠</div>
        </div>
        <div className={styles.subHeader}>
          <SubTabButton content1="리뷰" content2="도서"></SubTabButton>
        </div>
        <div className={styles.detailWrapper}>
          <div className={styles.grayText}>총 8건</div>
          <div className={styles.alignButton}>
            <AlignButton></AlignButton>
          </div>
        </div>
        <div className={styles.reviewWrapper}>
          <ReviewCardLists reviewCardLists={reviewCardLists}></ReviewCardLists>
        </div>
      </div>
    </div>
  );
}
