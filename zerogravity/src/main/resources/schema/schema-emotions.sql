DROP DATABASE IF EXISTS zerogravity;
CREATE DATABASE zerogravity DEFAULT CHARACTER SET utf8mb4;

USE zerogravity;

# 감정 정보
CREATE TABLE emotion (
    emotion_id VARCHAR(36) NOT NULL PRIMARY KEY,
    emotion_type VARCHAR(100) NOT NULL,
    emotion_level INT NOT NULL,
    color_scheme VARCHAR(100) NOT NULL,
    shape_name VARCHAR(100) NOT NULL,
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

# 감정 상세 정보
CREATE TABLE emotion_detail (
    emotion_detail_id VARCHAR(36) NOT NULL KEY,
    emotion_id VARCHAR(36) NOT NULL,
	emotion_detail VARCHAR(100) NOT NULL,
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (emotion_id) REFERENCES emotion(emotion_id) ON DELETE CASCADE ON UPDATE CASCADE
);