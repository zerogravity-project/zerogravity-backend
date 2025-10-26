# 사용자 기본 정보 (OAuth 로그인에 기반함)
# user_id: Snowflake ID (내부 DB ID, JWT용)
# provider_id: OAuth 제공자 ID (중복 가입 방지)
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