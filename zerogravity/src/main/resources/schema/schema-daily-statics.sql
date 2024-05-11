DROP DATABASE IF EXISTS zerogravity;
CREATE DATABASE zerogravity DEFAULT CHARACTER SET utf8mb4;

USE zerogravity;

CREATE TABLE daily_statics (
	daily_statics_id VARCHAR(36) PRIMARY KEY,
    user_id BIGINT,
    daily_sum INT,
    created_time DATETIME,
    updated_time DATETIME,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);