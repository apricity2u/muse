from requests import get
from dotenv import load_dotenv
import os
import math

load_dotenv()
PAGE_SIZE = 100
MAX_START = 1000


def fetch_api(query):
    URL = os.getenv("NAVER_API_URL")
    CLIENT_ID = os.getenv("NAVER_CLIENT_ID")
    CLIENT_SECRET = os.getenv("NAVER_CLIENT_SECRET")

    cur = 1
    total = math.inf
    items = []

    headers = {
        "X-Naver-Client-Id": CLIENT_ID,
        "X-Naver-Client-Secret": CLIENT_SECRET,
    }

    try:
        while cur < min(total, MAX_START):
            params = {"query": query, "display": PAGE_SIZE, "start": cur}
            response = get(url=URL, headers=headers, params=params)
            response.raise_for_status()
            data = response.json()

            total = int(data.get("total", 0))

            for li in data.get("items", []):
                item = {
                    "title": li.get("title", "").strip(),
                    "author": li.get("author", "").strip(),
                    "publisher": li.get("publisher", "").strip(),
                    "published_date": li.get("pubdate", "").strip(),
                    "image_url": li.get("image", "").strip(),
                    "isbn": li.get("isbn", "").strip(),
                    "description": li.get("description", "").strip(),
                }
                items.append(item)

            cur += PAGE_SIZE

    except Exception as e:
        print(f"[fetch_api] API 호출 중 오류 발생: {e}")

    return items
