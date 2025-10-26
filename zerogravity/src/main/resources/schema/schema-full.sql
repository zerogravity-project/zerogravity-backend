-- Database initialization
DROP DATABASE IF EXISTS zerogravity;
CREATE DATABASE zerogravity DEFAULT CHARACTER SET utf8mb4;

USE zerogravity;

-- 1. User table
-- Basic user information from OAuth login (Kakao)
-- user_id: Snowflake ID (internal DB ID for JWT)
-- provider_id: OAuth provider ID (prevent duplicate registration)
CREATE TABLE user (
    user_id BIGINT NOT NULL PRIMARY KEY,
    provider_id VARCHAR(255) NOT NULL,
    provider VARCHAR(50) NOT NULL,
    email VARCHAR(255),
    name VARCHAR(255) NOT NULL,
    image VARCHAR(255),
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY unique_provider_user (provider_id, provider)
);

-- 2. Emotion table
-- Emotion type definitions (7 levels)
-- emotion_id: 0-6 (VERY NEGATIVE to VERY POSITIVE)
-- emotion_key: snake_case key for frontend mapping
-- emotion_type: Display name for UI
-- emotion_level: Numerical level for statistics calculation (0-6)
CREATE TABLE emotion (
    emotion_id INT NOT NULL PRIMARY KEY,
    emotion_key VARCHAR(50) NOT NULL UNIQUE,
    emotion_type VARCHAR(50) NOT NULL UNIQUE,
    emotion_level INT NOT NULL,
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Insert default emotion data
INSERT INTO emotion (emotion_id, emotion_key, emotion_type, emotion_level) VALUES
(0, 'very_negative', 'VERY NEGATIVE', 0),
(1, 'negative', 'NEGATIVE', 1),
(2, 'mid_negative', 'MID NEGATIVE', 2),
(3, 'normal', 'NORMAL', 3),
(4, 'mid_positive', 'MID POSITIVE', 4),
(5, 'positive', 'POSITIVE', 5),
(6, 'very_positive', 'VERY POSITIVE', 6);

-- 3. Emotion record table
-- emotion_record_type: 'daily' or 'moment'
-- emotion_id: Reference to emotion table (0-6)
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

-- 4. Junction table for emotion reasons (many-to-many relationship)
-- Maps emotion records to their associated reasons
CREATE TABLE emotion_record_reason (
    emotion_record_id VARCHAR(36) NOT NULL,
    emotion_reason VARCHAR(50) NOT NULL,
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (emotion_record_id, emotion_reason),
    FOREIGN KEY (emotion_record_id) REFERENCES emotion_record(emotion_record_id) ON DELETE CASCADE
);

-- 5. Daily emotion statistics
-- Aggregated emotion data for each day per user
-- daily_sum: Sum of emotion levels
-- daily_count: Number of emotion records
-- daily_average: Average emotion level
CREATE TABLE daily_chart (
    daily_chart_id VARCHAR(36) NOT NULL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    daily_sum INT NOT NULL,
    daily_count INT NOT NULL,
    daily_average DOUBLE NOT NULL,
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(user_id)
);

-- 6. Periodic emotion statistics (weekly, monthly, yearly)
-- Aggregated emotion data for periods
-- period_type: 'weekly', 'monthly', or 'yearly'
-- period_sum: Sum of emotion levels in the period
-- period_count: Number of emotion records in the period
-- period_average: Average emotion level in the period
CREATE TABLE periodic_chart (
    periodic_chart_id VARCHAR(36) NOT NULL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    period_start DATETIME NOT NULL,
    period_end DATETIME NOT NULL,
    period_type VARCHAR(10) NOT NULL,
    periodic_sum INT NOT NULL,
    periodic_count INT NOT NULL,
    periodic_average DOUBLE NOT NULL,
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(user_id)
);