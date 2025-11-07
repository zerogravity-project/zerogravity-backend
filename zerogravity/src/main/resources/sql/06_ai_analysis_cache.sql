# AI Analysis Cache Table
# Stores cached AI-generated emotion analysis summaries
# Expires after 24 hours (TTL via expires_at)

CREATE TABLE ai_analysis_cache (
  cache_id BIGINT NOT NULL PRIMARY KEY,
  user_id BIGINT NOT NULL,
  period VARCHAR(10) NOT NULL,
  start_date DATE NOT NULL,
  end_date DATE NOT NULL,
  summary_json TEXT NOT NULL,
  generated_at TIMESTAMP NOT NULL,
  expires_at TIMESTAMP NOT NULL,
  FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE CASCADE,
  UNIQUE(user_id, period, start_date),
  INDEX idx_user_period (user_id, period),
  INDEX idx_expires (expires_at)
);
