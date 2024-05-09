DROP DATABASE IF EXISTS spaceout;
CREATE DATABASE spaceout DEFAULT CHARACTER SET utf8mb4;

USE spaceout;

# 사용자 기본 정보
CREATE TABLE user (
    user_id BIGINT NOT NULL PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    profile_nickname VARCHAR(255) NOT NULL,
    profile_image_url VARCHAR(255),
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

# 사용자 추가 동의 제공 정보
CREATE TABLE user_info (
	user_id BIGINT NOT NULL PRIMARY KEY,
	gender ENUM('male', 'female'),
    age_range VARCHAR(20),
    birthday VARCHAR(10), #MMDD
    birthyear VARCHAR(10), #YYYY
	FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE CASCADE ON UPDATE CASCADE
);

# 사용자 커스터마이징 설정 정보
CREATE TABLE user_setting (
    setting_id VARCHAR(36) NOT NULL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    object_id VARCHAR(36) NOT NULL,
    font_style VARCHAR(100) NOT NULL,
    color_scheme VARCHAR(100) NOT NULL,
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (object_id) REFERENCES object(object_id) ON UPDATE CASCADE
);