import clsx from 'clsx';
import styles from './RoundButton.module.css';

export default function RoundButton({ children, color = 'blue', clickHandler }) {
  return (
    <button onClick={clickHandler} className={clsx(styles.btn, styles[color])}>
      {children}
    </button>
  );
}
