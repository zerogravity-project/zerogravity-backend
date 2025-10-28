-- Database initialization and configuration
-- ============================================

-- 1. Set timezone to UTC for database
-- All timestamps will be stored as UTC, application converts to user timezone
SET GLOBAL time_zone = '+00:00';
SET time_zone = '+00:00';

-- 2. Create database
DROP DATABASE IF EXISTS zerogravity;
CREATE DATABASE zerogravity DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE zerogravity;

-- ============================================
-- Table Definitions
-- ============================================

-- 1. User table
-- Basic user information from OAuth login
-- user_id: Snowflake ID (64-bit, internal DB ID for JWT)
-- provider_id: OAuth provider ID (prevent duplicate registration)
-- Soft delete: is_deleted, deleted_at
CREATE TABLE user (
    user_id BIGINT NOT NULL PRIMARY KEY,
    provider_id VARCHAR(255) NOT NULL,
    provider VARCHAR(50) NOT NULL,
    email VARCHAR(255),
    name VARCHAR(255) NOT NULL,
    image VARCHAR(255),
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    deleted_at DATETIME NULL,
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY unique_provider_user (provider_id, provider)
);

-- 2. Emotion table
-- Emotion type definitions (7 levels: 0-6)
-- emotion_key: snake_case key for frontend mapping
-- emotion_type: Display name for UI
-- emotion_level: Numerical level for statistics calculation
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
-- Snowflake ID for emotion_record_id (BIGINT, 64-bit, up to 19 digits)
-- Soft delete: is_deleted, deleted_at
-- Indexes: (user_id, created_time), (user_id, is_deleted), (user_id, emotion_record_type, created_time)
CREATE TABLE emotion_record (
    emotion_record_id BIGINT NOT NULL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    emotion_id INT NOT NULL,
    emotion_record_type ENUM('daily', 'moment') NOT NULL,
    diary_entry TEXT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    deleted_at DATETIME NULL,
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE CASCADE,
    FOREIGN KEY (emotion_id) REFERENCES emotion(emotion_id),
    INDEX idx_user_created (user_id, created_time),
    INDEX idx_user_deleted (user_id, is_deleted),
    INDEX idx_user_type_date (user_id, emotion_record_type, created_time)
);

-- 4. Junction table for emotion reasons (many-to-many relationship)
-- Maps emotion records to their associated reasons
-- Emotion record ID changed to BIGINT (Snowflake)
CREATE TABLE emotion_record_reason (
    emotion_record_id BIGINT NOT NULL,
    emotion_reason VARCHAR(50) NOT NULL,
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (emotion_record_id, emotion_reason),
    FOREIGN KEY (emotion_record_id) REFERENCES emotion_record(emotion_record_id) ON DELETE CASCADE
);

-- ============================================
-- Database Configuration Verification
-- ============================================

-- Verify timezone settings are UTC
-- SELECT @@global.time_zone, @@session.time_zone;
-- Expected result: +00:00, +00:00
