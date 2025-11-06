# AI Emotion Analysis Storage
# Stores AI-generated emotion predictions and analysis results
# is_accepted = TRUE when user confirms and creates emotion_record
# emotion_record_id links to the actual emotion record created

CREATE TABLE emotion_ai_analysis (
  analysis_id BIGINT NOT NULL PRIMARY KEY,
  user_id BIGINT NOT NULL,
  diary_entry TEXT NOT NULL,
  suggested_emotion_id INT NULL,
  reasoning TEXT,
  confidence DECIMAL(3,2),
  analyzed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  is_accepted BOOLEAN NOT NULL DEFAULT FALSE,
  accepted_at TIMESTAMP NULL,
  emotion_record_id BIGINT NULL,
  FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE CASCADE,
  FOREIGN KEY (emotion_record_id) REFERENCES emotion_record(emotion_record_id) ON DELETE SET NULL,
  INDEX idx_user_analyzed (user_id, analyzed_at),
  INDEX idx_accepted (is_accepted)
);

# Junction table for AI-suggested emotion reasons
# Similar to emotion_record_reason but for AI predictions

CREATE TABLE emotion_ai_analysis_reason (
  analysis_id BIGINT NOT NULL,
  emotion_reason VARCHAR(50) NOT NULL,
  created_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (analysis_id, emotion_reason),
  FOREIGN KEY (analysis_id) REFERENCES emotion_ai_analysis(analysis_id) ON DELETE CASCADE
);
