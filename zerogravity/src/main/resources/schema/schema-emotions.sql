# Emotion type definitions (7 levels)
# emotion_id: 0-6 (VERY NEGATIVE to VERY POSITIVE)
# emotion_key: snake_case key for frontend mapping
# emotion_type: Display name for UI

CREATE TABLE emotion (
    emotion_id INT NOT NULL PRIMARY KEY,
    emotion_key VARCHAR(50) NOT NULL UNIQUE,
    emotion_type VARCHAR(50) NOT NULL UNIQUE,
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

# Insert default emotion data
INSERT INTO emotion (emotion_id, emotion_key, emotion_type) VALUES
(0, 'very_negative', 'VERY NEGATIVE'),
(1, 'negative', 'NEGATIVE'),
(2, 'mid_negative', 'MID NEGATIVE'),
(3, 'normal', 'NORMAL'),
(4, 'mid_positive', 'MID POSITIVE'),
(5, 'positive', 'POSITIVE'),
(6, 'very_positive', 'VERY POSITIVE');