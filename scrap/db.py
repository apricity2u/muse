from mysql.connector.pooling import MySQLConnectionPool
from mysql.connector import Error
from contextlib import contextmanager
from dotenv import load_dotenv
import os

load_dotenv()

DATABASE_HOST = os.getenv("DATABASE_HOST")
DATABASE_PORT = os.getenv("DATABASE_PORT")
DATABASE_NAME = os.getenv("DATABASE_NAME")
DATABASE_USER = os.getenv("DATABASE_USERNAME")
DATABASE_PASSWORD = os.getenv("DATABASE_PASSWORD")

pool = MySQLConnectionPool(
    pool_name="db_pool",
    pool_size=5,
    host=DATABASE_HOST,
    port=DATABASE_PORT,
    database=DATABASE_NAME,
    user=DATABASE_USER,
    password=DATABASE_PASSWORD,
)


@contextmanager
def get_connection():
    conn = pool.get_connection()
    try:
        yield conn
    finally:
        conn.close()


def fetch_one(query, params=None):
    try:
        with get_connection() as conn:
            with conn.cursor(buffered=True, dictionary=True) as cursor:
                cursor.execute(query, params or ())
                return cursor.fetchone()
    except Error as e:
        print(f"[fetch_one] 쿼리 실행 오류: {e}")
        return None


def execute_query(query, params=None):
    try:
        with get_connection() as conn:
            with conn.cursor(buffered=True) as cursor:
                cursor.execute(query, params or ())
                conn.commit()
                if cursor.lastrowid:
                    return cursor.lastrowid
                return cursor.rowcount
    except Error as e:
        print(f"[execute_query] 쿼리 실행 오류: {e}")
        return None


def get_book_by_title_author(title: str, author: str):
    sql = """
        SELECT id
        FROM book
        WHERE title = %s
          AND author = %s
        LIMIT 1
    """
    return fetch_one(sql, (title, author))


def insert_book(
    title: str,
    author: str,
    publisher: str,
    description: str,
    image_url: str,
    isbn: str,
    published_date: str,
    title_normalized: str,
):
    sql = """
        INSERT INTO book
          (title,
           title_normalized,
           author,
           publisher,
           description,
           image_url,
           isbn,
           published_date)
        VALUES (%s, %s, %s, %s, %s, %s, %s, %s)
    """
    params = (
        title,
        title_normalized,
        author,
        publisher,
        description,
        image_url,
        isbn,
        published_date if published_date else None,
    )
    return execute_query(sql, params)
