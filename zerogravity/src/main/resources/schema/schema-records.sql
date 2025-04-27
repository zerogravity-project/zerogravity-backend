# 감정 기록 정보
CREATE TABLE emotion_record (
    emotion_record_id VARCHAR(36) NOT NULL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    emotion_reason VARCHAR(36) NOT NULL,
    emotion_record_type VARCHAR(36) NOT NULL,
    emotion_record_level INT NOT NULL,
    emotion_record_state VARCHAR(36) NOT NULL,
    diary_entry TEXT NULL,
	created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
	updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE CASCADE ON UPDATE CASCADE
);