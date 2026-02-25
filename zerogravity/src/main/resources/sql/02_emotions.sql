# Emotion type definitions (7 levels)
# emotion_id: 0-6 (VERY NEGATIVE to VERY POSITIVE)
# emotion_key: snake_case key for frontend mapping
# emotion_type: Display name for UI
# emotion_level: Numerical level for statistics calculation (0-6)

CREATE TABLE emotion (
    emotion_id INT NOT NULL PRIMARY KEY,
    emotion_key VARCHAR(50) NOT NULL UNIQUE,
    emotion_type VARCHAR(50) NOT NULL UNIQUE,
    emotion_level INT NOT NULL,
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

# Insert default emotion data
INSERT INTO emotion (emotion_id, emotion_key, emotion_type, emotion_level) VALUES
(0, 'very_negative', 'VERY NEGATIVE', 0),
(1, 'negative', 'NEGATIVE', 1),
(2, 'slightly_negative', 'SLIGHTLY NEGATIVE', 2),
(3, 'normal', 'NORMAL', 3),
(4, 'slightly_positive', 'SLIGHTLY POSITIVE', 4),
(5, 'positive', 'POSITIVE', 5),
(6, 'very_positive', 'VERY POSITIVE', 6);