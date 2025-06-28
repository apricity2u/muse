import clsx from 'clsx';
import styles from './ImageUploader.module.css';
import deleteFileIcon from '../../assets/icons/trash.png';
import addFileIcon from '../../assets/icons/add-document.png';
import basic from '../../assets/basic.jpg';
import { useRef } from 'react';

export default function ImageUploader({ image, originalFileName, setReview, setUpdatedReview }) {
  const fileInputRef = useRef(null);

  const deleteFileHandler = () => {
    setReview((prev) => ({ ...prev, imageUrl: basic, image: null, originalFileName: '' }));
    setUpdatedReview((prev) => ({ ...prev, image: null }));
  };

  const handleDrop = async (e) => {
    handleDragEvent(e);
    const droppedFile = e.dataTransfer.files?.[0];
    if (!droppedFile) return;

    if (!droppedFile.type.startsWith('image/')) {
      alert('이미지 파일만 업로드 가능합니다.');
      return;
    }

    try{
      const processedImage = await processImage(droppedFile);
      updateImage(processedImage);
    } catch (error) {
      console.error('이미지 처리 중 오류:', error);
      alert('이미지 처리 중 오류가 발생했습니다.');
    }
  };

  const handleDragEvent = (e) => {
    e.preventDefault();
    e.stopPropagation();
  };

  const attachFileHandler = () => {
    fileInputRef.current?.click();
  };

  const fileChangeHandler = async (e) => {
    const file = e.target.files[0];
    if (!file) {
      return;
    }
    
    try {
      const processedImage = await processImage(file);
      updateImage(processedImage);
    } catch (error) {
      console.error('이미지 처리 중 오류:', error);
      alert('이미지 처리 중 오류가 발생했습니다.');
    }
  };
  
    const processImage = (file) => {
      return new Promise((resolve, reject) => {
        const image = new Image();
        const url = URL.createObjectURL(file);
        image.onload = () => {
          URL.revokeObjectURL(url);

          const maxWidth = 800;
          const canvas = document.createElement('canvas');
          const ctx = canvas.getContext('2d');
          canvas.width = image.width;
          canvas.height = image.height;

          if (image.width > maxWidth) {
            const ratio = maxWidth / image.width;
            canvas.width = image.width * ratio;
            canvas.height = image.height * ratio;
          }
          ctx.drawImage(image, 0, 0, canvas.width, canvas.height);

          canvas.toBlob((blob) => {
            if (!blob) {
              reject(new Error('Blob 생성 실패'));
              return;
            }

            const newFile = new File([blob], file.name.replace(/\.\w+$/, '.webp'), {
              type: 'image/webp',
            });
            resolve(newFile);
          }, 'image/webp', 0.8);
        };
        image.onerror = () => {
          URL.revokeObjectURL(url);
          reject(new Error('이미지 로드 실패'));
        };
        image.src = url;
      })
    }

  const updateImage = (file) => {
    const imageUrl = URL.createObjectURL(file);
    setReview((prev) => ({ ...prev, image: file, originalFileName: file.name, imageUrl }));
    setUpdatedReview((prev) => ({ ...prev, updatedImage: file }));
  };

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
