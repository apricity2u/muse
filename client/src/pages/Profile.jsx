import React, { useEffect, useRef, useState } from 'react';
import RoundButton from '../components/common/button/RoundButton';
import styles from './Profile.module.css';
import profileApi from '../api/profileApi';
import { useDispatch, useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import { changeProfile } from '../store/slices/authSlice';

export default function Profile() {
  const MAX_LENGTH = 20;
  const memberId = useSelector((state) => state.auth.memberId);
  const navigate = useNavigate();
  const dispatch = useDispatch();

  const [initialData, setInitialData] = useState({
    nickname: '',
    profileImageUrl: '',
  });

  const [formData, setFormData] = useState({
    nickname: '',
    imageFile: null,
  });

  const [previewUrl, setPreviewUrl] = useState(null);
  const imageFileRef = useRef(null);
  const nicknameRef = useRef(null);
  const [nicknameCount, setNicknameCount] = useState(0);

  useEffect(() => {
    const getProfile = async () => {
      try {
        const response = await profileApi.getProfile(memberId);
        const { nickname, profileImageUrl } = response.data;

        setInitialData({ nickname, profileImageUrl });
        setFormData((prev) => ({ ...prev, nickname }));
        setNicknameCount(nickname.length);
      } catch (error) {
        console.log(error);
      }
    };
    getProfile();
    return () => {
      if (previewUrl) {
        URL.revokeObjectURL(previewUrl);
      }
    };
  }, []);

  const handleNicknameChange = (e) => {
    const nickname = e.target.value;
    setFormData((prev) => ({ ...prev, nickname: nickname }));
    setNicknameCount(nickname.length);
  };
  const onClickImageButton = () => {
    if (imageFileRef.current) {
      imageFileRef.current.click();
    }
  };

  const onChangeImageFile = async (e) => {
    const imageFile = e.target.files[0];
    if (!imageFile) return;
    if (!imageFile.type.startsWith('image/')) {
      alert('이미지 파일만 업로드 가능합니다.');
      return;
    }

    try {

    
    if (previewUrl) URL.revokeObjectURL(previewUrl);

    const processedImage = await processImage(imageFile);
    setFormData((prev) => ({ ...prev, imageFile: processedImage }));
    const objectUrl = URL.createObjectURL(processedImage);
    setPreviewUrl(objectUrl);
    } catch (error) {
      console.error('이미지 처리 중 오류:', error);
      alert('이미지 처리 중 오류가 발생했습니다.');
    }
  };
  
  const processImage = async (file) => {
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

        canvas.toBlob(
          (blob) => {
            if (!blob) {
              reject(new Error('Blob 생성 실패'));
              return;
            }

            const newFile = new File([blob], file.name.replace(/\.\w+$/, '.webp'), {
              type: 'image/webp',
            });
            resolve(newFile);
          },
          'image/webp',
          0.8,
        );
      };
      image.onerror = () => {
        URL.revokeObjectURL(url);
        reject(new Error('이미지 로드 실패'));
      };
      image.src = url;
    });
  };
  const handleSubmit = async () => {
    try {
      const data = new FormData();
      data.append('nickname', formData.nickname);
      data.append('image', formData.imageFile);
      const response = await profileApi.updateProfile(memberId, data);
      dispatch(changeProfile(response.data));
      navigate(`/users/${memberId}`);
    } catch (error) {
      console.log(error);
    }
  };

  return (
    <div className={styles.wrapper}>
      <input
        type="file"
        accept="image/*"
        ref={imageFileRef}
        onChange={onChangeImageFile}
        style={{ display: 'none' }}
        id="input"
      />
      <img src={previewUrl || initialData.profileImageUrl} className={styles.profileImage} />
      <RoundButton color="primary700" clickHandler={onClickImageButton}>
        <span style={{ color: `var(--primary-500)` }}>사진 수정</span>
      </RoundButton>
      <div className={styles.formField}>
        <label htmlFor="nicknameInput" className={styles.label}>
          닉네임
        </label>
        <input
          type="text"
          id="nicknameInput"
          className={styles.input}
          value={formData.nickname}
          onChange={handleNicknameChange}
          maxLength={MAX_LENGTH}
          ref={nicknameRef}
        />
        <div className={styles.warning}>
          {/* <span>특수문자는 사용이 불가능합니다.</span> */}
          <span>
            {nicknameCount}/{MAX_LENGTH}
          </span>
        </div>
      </div>
      <RoundButton clickHandler={handleSubmit}>수정</RoundButton>
    </div>
  );
}
