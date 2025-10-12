-- 1) image: review 기반 커버링 인덱스 (리뷰 이미지 조회, image_url 포함)
SET @exists_image_review := (
  SELECT COUNT(1)
  FROM INFORMATION_SCHEMA.STATISTICS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'image'
    AND INDEX_NAME = 'idx_image_review_type_created_imageurl'
);

SET @sql_image_review := IF(@exists_image_review = 0,
  'CREATE INDEX idx_image_review_type_created_imageurl ON image (review_id, image_type, created_at, image_url)',
  'SELECT 1'
);

PREPARE stmt_image_review FROM @sql_image_review;
EXECUTE stmt_image_review;
DEALLOCATE PREPARE stmt_image_review;


-- 2) image: member 기반 커버링 인덱스 (프로필 최신 이미지 조회용)
SET @exists_image_member := (
  SELECT COUNT(1)
  FROM INFORMATION_SCHEMA.STATISTICS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'image'
    AND INDEX_NAME = 'idx_image_member_type_created_imageurl'
);

SET @sql_image_member := IF(@exists_image_member = 0,
  'CREATE INDEX idx_image_member_type_created_imageurl ON image (member_id, image_type, created_at, image_url)',
  'SELECT 1'
);

PREPARE stmt_image_member FROM @sql_image_member;
EXECUTE stmt_image_member;
DEALLOCATE PREPARE stmt_image_member;


-- 3) likes: (review_id, member_id) - EXISTS 및 review별 count 보조
SET @exists_likes_review_member := (
  SELECT COUNT(1)
  FROM INFORMATION_SCHEMA.STATISTICS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'likes'
    AND INDEX_NAME = 'idx_likes_review_member'
);

SET @sql_likes_review_member := IF(@exists_likes_review_member = 0,
  'CREATE INDEX idx_likes_review_member ON likes (review_id, member_id)',
  'SELECT 1'
);

PREPARE stmt_likes_review_member FROM @sql_likes_review_member;
EXECUTE stmt_likes_review_member;
DEALLOCATE PREPARE stmt_likes_review_member;


-- 4) likes: (book_id, member_id) - book EXISTS 및 book별 count 보조
SET @exists_likes_book_member := (
  SELECT COUNT(1)
  FROM INFORMATION_SCHEMA.STATISTICS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'likes'
    AND INDEX_NAME = 'idx_likes_book_member'
);

SET @sql_likes_book_member := IF(@exists_likes_book_member = 0,
  'CREATE INDEX idx_likes_book_member ON likes (book_id, member_id)',
  'SELECT 1'
);

PREPARE stmt_likes_book_member FROM @sql_likes_book_member;
EXECUTE stmt_likes_book_member;
DEALLOCATE PREPARE stmt_likes_book_member;
