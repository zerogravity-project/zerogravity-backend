DROP DATABASE IF EXISTS zerogravity;
CREATE DATABASE zerogravity DEFAULT CHARACTER SET utf8mb4;

USE zerogravity;

# 감정 통계 정보
CREATE TABLE emotion_statics (
    emotion_statics_id VARCHAR(36) NOT NULL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    avg_level_weekly DOUBLE NOT NULL,
    avg_level_monthly DOUBLE NOT NULL,
    avg_level_yearly DOUBLE NOT NULL,
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE CASCADE ON UPDATE CASCADE
);