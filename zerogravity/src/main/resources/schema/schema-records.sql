DROP DATABASE IF EXISTS zerogravity;
CREATE DATABASE zerogravity DEFAULT CHARACTER SET utf8mb4;

USE zerogravity;

# 감정 기록 정보
CREATE TABLE emotion_record (
    emotion_record_id VARCHAR(36) NOT NULL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    emotion_detail_id VARCHAR(36) NOT NULL,
    diary_entry TEXT NULL,
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (emotion_detail_id) REFERENCES emotion_detail(emotion_detail_id) ON DELETE CASCADE ON UPDATE CASCADE
);