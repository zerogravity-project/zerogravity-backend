# Add refined_diary column to emotion_ai_analysis table
# This stores AI-cleaned/clarified version of the original diary entry
# Max 300 characters as per diary entry validation rules

ALTER TABLE emotion_ai_analysis
ADD COLUMN refined_diary VARCHAR(300) NULL COMMENT 'AI-refined version of diary entry (max 300 chars)';
