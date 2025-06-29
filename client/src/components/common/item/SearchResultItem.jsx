import styles from './SearchResultItem.module.css';

export default function SearchResultItem({ id, title, keyword, clickHandler }) {
  const highlightText = (text, keyword) => {
    if (!keyword) return text;

    const regex = new RegExp(`(${keyword})`, 'gi');
    const parts = text.split(regex);

    return parts.map((part, index) =>
      part.toLowerCase() === keyword.toLowerCase() ? (
        <span key={index} style={{ color: 'black', fontWeight: 'bold' }}>
          {part}
        </span>
      ) : (
        <span key={index}>{part}</span>
      ),
    );
  };

  return (
    <li id={id} onClick={clickHandler} className={styles.searchResult}>
      {highlightText(title, keyword)}
    </li>
  );
}
