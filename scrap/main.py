from api import fetch_api
from db import get_book_by_title_author, insert_book
from datetime import datetime


def normalize_title(title: str) -> str:
    return title.replace(" ", "").lower()


def process_query(query_char: str):
    items = fetch_api(query_char)
    if not items:
        return

    for item in items:
        title = item.get("title", "")
        author = item.get("author", "")
        publisher = item.get("publisher", "")
        description = item.get("description", "")
        image_url = item.get("image_url", "")
        isbn = item.get("isbn", "")
        raw_date = item.get("published_date", "")

        published_date = None
        if raw_date and len(raw_date) == 8:
            try:
                dt = datetime.strptime(raw_date, "%Y%m%d")
                published_date = dt.strftime("%Y-%m-%d")
            except:
                published_date = None

        title_normalized = normalize_title(title)

        existing = get_book_by_title_author(title, author)
        if existing:
            continue

        insert_book(
            title=title,
            author=author,
            publisher=publisher,
            description=description,
            image_url=image_url,
            isbn=isbn,
            published_date=published_date,
            title_normalized=title_normalized,
        )


for codepoint in range(ord("가"), ord("핳") + 1):
    ch = chr(codepoint)
    process_query(ch)
