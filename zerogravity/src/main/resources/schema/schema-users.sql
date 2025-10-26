# User basic information (OAuth-based login)
# user_id: Snowflake ID (internal DB ID, for JWT)
# provider_id: OAuth provider ID (prevent duplicate registration)

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