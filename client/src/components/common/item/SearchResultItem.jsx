import { forwardRef } from 'react';
import styles from './SearchResultItem.module.css';

const SearchResultItem = forwardRef(function SearchResultItem(
  { id, title, keyword, clickHandler, isFocused },
  ref,
) {
  const highlightText = (text, keyword) => {
    if (!keyword) return text;
    const regex = new RegExp(`(${keyword})`, 'gi');
    const parts = text.split(regex);
    return parts.map((part, index) =>
      regex.test(part) ? (
        <span key={index} className={styles.highlight}>
          {part}
        </span>
      ) : (
        part
      ),
    );
  };

  return (
    <li
      id={id}
      ref={ref}
      onClick={clickHandler}
      className={`${styles.searchResult} ${isFocused ? styles.focused : ''}`}
    >
      {highlightText(title, keyword)}
    </li>
  );
});

export default SearchResultItem;
