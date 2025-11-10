-- Refresh Tokens Table
-- Stores refresh tokens for JWT token renewal
-- Implements token rotation and reuse detection for security

CREATE TABLE IF NOT EXISTS refresh_tokens (
    token_id BIGINT PRIMARY KEY COMMENT 'Snowflake ID for unique token identification',
    user_id BIGINT NOT NULL COMMENT 'Foreign key to users table',
    refresh_token VARCHAR(255) NOT NULL UNIQUE COMMENT 'UUID-based refresh token string',
    expires_at DATETIME NOT NULL COMMENT 'Expiration timestamp (30 days from creation)',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT 'Token creation timestamp',
    used_at DATETIME NULL COMMENT 'Last used timestamp for rotation tracking',
    is_revoked BOOLEAN DEFAULT FALSE COMMENT 'Revocation flag for logout/security events',

    INDEX idx_user_id (user_id),
    INDEX idx_refresh_token (refresh_token),
    INDEX idx_expires_at (expires_at),

    FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Refresh tokens for secure token renewal';
