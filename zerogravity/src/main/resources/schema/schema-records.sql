# Emotion record information
# emotion_record_type: 'daily' or 'moment'
# emotion_id: Reference to emotion table (0-6)

CREATE TABLE emotion_record (
    emotion_record_id VARCHAR(36) NOT NULL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    emotion_id INT NOT NULL,
    emotion_record_type ENUM('daily', 'moment') NOT NULL,
    diary_entry TEXT NULL,
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE CASCADE,
    FOREIGN KEY (emotion_id) REFERENCES emotion(emotion_id)
);

# Junction table for emotion reasons (many-to-many relationship)
# Maps emotion records to their associated reasons

CREATE TABLE emotion_record_reason (
    emotion_record_id VARCHAR(36) NOT NULL,
    emotion_reason VARCHAR(50) NOT NULL,
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (emotion_record_id, emotion_reason),
    FOREIGN KEY (emotion_record_id) REFERENCES emotion_record(emotion_record_id) ON DELETE CASCADE
);