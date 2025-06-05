import React, { useEffect, useRef, useState } from 'react';
import RoundButton from '../components/common/button/RoundButton';
import styles from './Profile.module.css';
import profileApi from '../api/profileApi';
import { useSelector } from 'react-redux';

export default function Profile() {
  const MAX_LENGTH = 20;
  const memberId = useSelector((state) => state.auth.memberId);

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

  const onChangeImageFile = (e) => {
    const imageFile = e.target.files[0];
    if (!imageFile) return;

    setFormData((prev) => ({ ...prev, imageFile }));
    const objectUrl = URL.createObjectURL(imageFile);
    setPreviewUrl(objectUrl);
  };

  const handleSubmit = async () => {
    try {
      const data = new FormData();
      data.append('nickname', formData.nickname);
      data.append('imageFile', formData.imageFile);
      const response = await profileApi.updateProfile(memberId, formData);
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
