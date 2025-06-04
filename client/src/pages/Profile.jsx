import React from 'react';
import RoundButton from '../components/common/button/RoundButton';
import styles from './Profile.module.css';

export default function Profile() {
  return (
    <div className={styles.wrapper}>
      <img
        src="https://recipe1.ezmember.co.kr/cache/recipe/2019/07/29/81147460a9faf7bdb78740b34758a5651.jpg"
        className={styles.profileImage}
      />
      <RoundButton color="primary700">
        <span style={{ color: `var(--primary-500)` }}>사진 수정</span>
      </RoundButton>
      <div className={styles.formField}>
        <label htmlFor="input" className={styles.label}>
          닉네임
        </label>
        <input type="text" id="input" className={styles.input} />
        <div className={styles.warning}>
          <span>특수문자는 사용이 불가능합니다.</span>
          <span>0/20</span>
        </div>
      </div>
      <RoundButton>수정</RoundButton>
    </div>
  );
}
