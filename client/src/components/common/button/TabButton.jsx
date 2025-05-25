import styles from './TabButton.module.css';

export default function TabButton({ children, clickHandler }) {
  return (
    <div onClick={clickHandler} className={styles.btn}>
      {children}
    </div>
  );
}
