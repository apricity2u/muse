import styles from './DropBoxButton.module.css';
import AlertModal from '../modal/AlertModal';

export default function DropBoxButton({ clickHandler1, clickHandler2, openModal, setOpenModal }) {
  const modalHandler = () => {
    setOpenModal(true);
  };

  const closeModalHandler = () => {
    setOpenModal(false);
  };

  return (
    <>
      {openModal && (
        <AlertModal clickHandler1={closeModalHandler} clickHandler2={clickHandler2}>
          리뷰를 삭제하시겠습니까?
        </AlertModal>
      )}
      <div className={styles.wrapper}>
        <div className={styles.btn} onClick={clickHandler1}>
          수정
        </div>
        <div className={styles.btn} onClick={modalHandler}>
          삭제
        </div>
      </div>
    </>
  );
}
