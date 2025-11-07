# Add foreign key constraints after all tables are created
# This resolves circular dependency between emotion_ai_analysis and emotion_record

# Add FK: emotion_ai_analysis.emotion_record_id -> emotion_record.emotion_record_id
ALTER TABLE emotion_ai_analysis
ADD CONSTRAINT fk_emotion_ai_analysis_record
FOREIGN KEY (emotion_record_id) REFERENCES emotion_record(emotion_record_id) ON DELETE SET NULL;
