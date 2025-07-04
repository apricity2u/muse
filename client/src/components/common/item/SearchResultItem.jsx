import styles from './SearchResultItem.module.css';

export default function SearchResultItem({ id, title, keyword, clickHandler }) {
  const highlightText = (text, keyword) => {
    if (!keyword) return text;

    const regex = new RegExp(`(${keyword})`, 'gi');
    const parts = text.split(regex);

    return parts.map((part, index) => {
      const isMatch = regex.test(part);
      return isMatch ? (
        <span key={index} className={styles.highlight}>
          {part}
        </span>
      ) : (
        part
      );
    });
  };

  return (
    <li id={id} onClick={clickHandler} className={styles.searchResult}>
      {highlightText(title, keyword)}
    </li>
  );
}
