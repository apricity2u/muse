import clsx from 'clsx';
import styles from './ImageUploader.module.css';
import deleteFileIcon from '../../assets/icons/trash.png';
import addFileIcon from '../../assets/icons/add-document.png';
import { useRef } from 'react';

export default function ImageUploader({ image, originalFileName, setReview, setUpdatedReview }) {
  const fileInputRef = useRef(null);

  const deleteFileHandler = () => {
    setReview((prev) => ({ ...prev, imageUrl: basic, image: null, originalFileName: '' }));
    setUpdatedReview((prev) => ({ ...prev, image: null }));
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

  const updateImage = (file) => {
    // revokeImage();

    const imageUrl = URL.createObjectURL(file);
    setReview((prev) => ({ ...prev, image: file, originalFileName: file.name, imageUrl }));
    setUpdatedReview((prev) => ({ ...prev, image: file }));
  };

  // TODO : 메모리 누수 관련 로직 점검

  // useEffect(() => {
  //   revokeImage();
  // }, [imageUrl]);

  // const revokeImage = () => {
  //   if (imageUrl && imageUrl.startsWith('blob:')) {
  //     URL.revokeObjectURL(imageUrl);
  //   }
  // };

  return (
    <>
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
          <div className={clsx(styles.grayText, styles.wordBreak)}>{originalFileName}</div>
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
    </>
  );
}
