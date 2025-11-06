# User basic information (OAuth-based login)
# user_id: Snowflake ID (internal DB ID, for JWT)
# provider_id: OAuth provider ID (prevent duplicate registration)
# Soft delete: is_deleted, deleted_at

CREATE TABLE user (
    user_id BIGINT NOT NULL PRIMARY KEY,
    provider_id VARCHAR(255) NOT NULL,
    provider VARCHAR(50) NOT NULL,
    email VARCHAR(255),
    name VARCHAR(255) NOT NULL,
    image VARCHAR(255),
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    deleted_at DATETIME NULL,

    -- User Consent Fields
    terms_agreed BOOLEAN NOT NULL DEFAULT FALSE,
    terms_agreed_at DATETIME NULL,
    privacy_agreed BOOLEAN NOT NULL DEFAULT FALSE,
    privacy_agreed_at DATETIME NULL,
    sensitive_data_consent BOOLEAN NOT NULL DEFAULT FALSE,
    sensitive_data_consent_at DATETIME NULL,
    ai_analysis_consent BOOLEAN DEFAULT FALSE,
    ai_analysis_consent_at DATETIME NULL,
    consent_version VARCHAR(20) DEFAULT 'v1.0',

    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY unique_provider_user (provider_id, provider)
);
