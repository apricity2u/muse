import styles from './SearchResultItem.module.css';

export default function SearchResultItem({ id, title, clickHandler }) {
  return (
    <li id={id} onClick={clickHandler} className={styles.searchResult}>
      {title}
    </li>
  );
}
