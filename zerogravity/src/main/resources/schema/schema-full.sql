-- DB 초기화
DROP DATABASE IF EXISTS zerogravity;
CREATE DATABASE zerogravity DEFAULT CHARACTER SET utf8mb4;

USE zerogravity;

-- 1. scene_object 테이블
CREATE TABLE scene_object (
    scene_object_id VARCHAR(36) NOT NULL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    model_url VARCHAR(255) NOT NULL,
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 2. user 테이블
CREATE TABLE user (
    user_id BIGINT NOT NULL PRIMARY KEY,
    nickname VARCHAR(255) NOT NULL,
    profile_image VARCHAR(255),
    thumbnail_image VARCHAR(255),
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 3. user_info 테이블
CREATE TABLE user_info (
	user_id BIGINT NOT NULL PRIMARY KEY,
	gender ENUM('male', 'female'),
    age_range VARCHAR(20),
    birthday VARCHAR(10), #MMDD
    birthyear VARCHAR(10), #YYYY
	FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- 4. user_setting 테이블
CREATE TABLE user_setting (
    user_setting_id VARCHAR(36) NOT NULL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    scene_object_id VARCHAR(36) NOT NULL,
    font_style VARCHAR(100) NOT NULL,
    color_scheme VARCHAR(100) NOT NULL,
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (scene_object_id) REFERENCES scene_object(scene_object_id) ON UPDATE CASCADE
);

-- 5. emotion 테이블
CREATE TABLE emotion (
    emotion_id VARCHAR(36) NOT NULL PRIMARY KEY,
    emotion_type VARCHAR(100) NOT NULL,
    emotion_level INT NOT NULL,
    color_scheme VARCHAR(100) NOT NULL,
    shape_name VARCHAR(100) NOT NULL,
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 6. emotion_reason 테이블
CREATE TABLE emotion_reason (
    emotion_reason_id VARCHAR(36) NOT NULL KEY,
	emotion_reason VARCHAR(100) NOT NULL,
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 7. daily_chart 테이블
CREATE TABLE daily_chart (
	daily_chart_id VARCHAR(36) NOT NULL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    daily_sum INT NOT NULL,
    daily_count INT NOT NULL, 
    daily_average DOUBLE NOT NULL,
	created_time  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
	updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(user_id)
);

-- 8. periodic_chart 테이블
CREATE TABLE periodic_chart (
	periodic_chart_id VARCHAR(36) PRIMARY KEY,
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

-- 9. emotion_record 테이블
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