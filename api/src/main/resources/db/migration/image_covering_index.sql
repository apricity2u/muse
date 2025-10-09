SET @exists_review := (
  SELECT COUNT(1)
  FROM INFORMATION_SCHEMA.STATISTICS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'image'
    AND INDEX_NAME = 'idx_image_review_type_created_imageurl'
);

SET @sql_review := IF(@exists_review = 0,
  'CREATE INDEX idx_image_review_type_created_imageurl ON image (review_id, image_type, created_at, image_url)',
  'SELECT 1'
);

PREPARE stmt_review FROM @sql_review;
EXECUTE stmt_review;
DEALLOCATE PREPARE stmt_review;



SET @exists_member := (
  SELECT COUNT(1)
  FROM INFORMATION_SCHEMA.STATISTICS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'image'
    AND INDEX_NAME = 'idx_image_member_type_imageurl'
);

SET @sql_member := IF(@exists_member = 0,
  'CREATE INDEX idx_image_member_type_imageurl ON image (member_id, image_type, image_url)',
  'SELECT 1'
);

PREPARE stmt_member FROM @sql_member;
EXECUTE stmt_member;
DEALLOCATE PREPARE stmt_member;
