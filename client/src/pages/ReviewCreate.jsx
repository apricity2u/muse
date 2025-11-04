import clsx from 'clsx';
import styles from './ReviewCreate.module.css';
import { useEffect, useRef, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import SearchBoard from '../components/common/board/SearchBoard';
import BookDashboard from '../components/review_create/BookDashboard';
import RoundButton from '../components/common/button/RoundButton';
import Card from '../components/review_create/Card';
import deleteFileIcon from '../assets/icons/trash.png';
import addFileIcon from '../assets/icons/add-document.png';
import bookApi from '../api/bookApi';
import reviewApi from '../api/reviewApi';
import dog from '../assets/dog.jpg';

export default function ReviewCreate() {
  const navigate = useNavigate();
  const { reviewId } = useParams();

  const [review, setReview] = useState({
    imageUrl: dog, // TODO: 기본이미지 선택 후 변경
    content: '',
  });
  const [bookDetail, setBookDetail] = useState({
    id: '',
    imageUrl: '',
    title: '',
    author: '',
    publisher: '',
    publicationDate: '',
    isbn: '',
  });

  const [isCreated, setIsCreated] = useState(false);
  const [formData, setFormData] = useState({
    image: null,
    content: '',
  });
  const fileInputRef = useRef(null);

  const { image, content } = formData;

  const clickResultHandler = async (e) => {
    const bookId = e.target.id;

    try {
      const response = await bookApi.getBook(bookId);
      const data = response.data;

      setBookDetail(data);
      setIsCreated(true);
    } catch (error) {
      // TODO: 에러 보완
      alert('도서 검색 중 오류가 발생했습니다.');
      console.error(error);
    }
  };

  const deleteFileHandler = () => {
    setFormData((prev) => ({ ...prev, image: null }));
    setReview((prev) => ({ ...prev, imageUrl: '' }));
  };

  const handleDrop = (e) => {
    handleDragEvent(e);
    const droppedFile = e.dataTransfer.files?.[0];
    if (droppedFile) updateImage(droppedFile);
  };

  const handleDragEvent = (e) => {
    e.preventDefault();
    e.stopPropagation();
  };

  const attachFileHandler = () => {
    fileInputRef.current?.click();
  };

  const fileChangeHandler = (e) => {
    const file = e.target.files[0];
    if (file) {
      updateImage(file);
    }
  };

  // TODO : 메모리 누수 관련 로직 점검

  // useEffect(() => {
  //   revokeImage();
  // }, [review.imageUrl]);

  // const revokeImage = () => {
  //   if (review.imageUrl && review.imageUrl.startsWith('blob:')) {
  //     URL.revokeObjectURL(review.imageUrl);
  //   }
  // };

  const updateImage = (file) => {
    // revokeImage();

    const imageUrl = URL.createObjectURL(file);
    setFormData((prev) => ({ ...prev, image: file }));
    setReview((prev) => ({ ...prev, imageUrl }));
  };

  const inputHandler = (e) => {
    const inputValue = e.target.value.slice(0, 50);
    setFormData((prev) => ({ ...prev, content: inputValue }));
    setReview((prev) => ({ ...prev, content: inputValue }));
  };

  const submitHandler = async () => {
    if (!content.trim()) {
      alert('리뷰 내용을 입력해주세요.');
      return;
    }

    try {
      await reviewApi.postReview(bookDetail.id, formData);
      navigate(`/books/${bookDetail.id}`);
    } catch (error) {
      // TODO: 에러 보완
      alert('리뷰 등록 중 오류가 발생했습니다.');
      console.error(error);
    }
  };

  return (
    <div className={styles.container}>
      {!reviewId && !isCreated ? (
        <>
          <div className={styles.title}>글쓰기</div>
          <div className={styles.searchWrapper}>
            <SearchBoard clickHandler={clickResultHandler}></SearchBoard>
          </div>
        </>
      ) : (
        <div className={styles.container2}>
          <div className={styles.previewWrapper}>
            <h2>미리보기</h2>
            <Card review={review}></Card>
          </div>
          <div className={styles.reviewWrapper}>
            <div className={styles.wrapper}>
              <h2>도서</h2>
              <BookDashboard bookDetail={bookDetail}></BookDashboard>
            </div>
            <div className={styles.wrapper}>
              <div className={styles.flexBox}>
                <h2>이미지</h2>
                <div className={styles.flexBox} onClick={deleteFileHandler}>
                  <img src={deleteFileIcon} alt="deleteFile" className={styles.icon} />
                  <span className={styles.grayText}>이미지 삭제하기</span>
                </div>
              </div>
              <div
                className={styles.imageWrapper}
                onClick={attachFileHandler}
                onDrop={handleDrop}
                onDragOver={handleDragEvent}
                onDragLeave={handleDragEvent}
              >
                {image ? (
                  <div className={clsx(styles.grayText, styles.wordBreak)}>{image.name}</div>
                ) : (
                  <>
                    <img src={addFileIcon} alt="addFile" className={styles.attachIcon} />
                    <span className={styles.grayText}>Drag and drop or browse</span>
                  </>
                )}
                <input
                  type="file"
                  accept="image/*"
                  ref={fileInputRef}
                  onChange={fileChangeHandler}
                  style={{ display: 'none' }}
                />
              </div>
            </div>
            <div className={styles.wrapper}>
              <h2>리뷰</h2>
              <textarea
                placeholder="이 책에 대한 한 줄 리뷰를 입력해주세요."
                value={content}
                maxLength={50}
                className={styles.textareaStyle}
                onChange={inputHandler}
              />
              <div className={styles.alignLeft}>{content.length}/50</div>
            </div>
            <div className={styles.alignLeft}>
              <RoundButton color="primary100" clickHandler={submitHandler}>
                등록
              </RoundButton>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
