DROP DATABASE IF EXISTS zerogravity;
CREATE DATABASE zerogravity DEFAULT CHARACTER SET utf8mb4;

USE zerogravity;

CREATE TABLE daily_statics (
	daily_statics_id VARCHAR(36) NOT NULL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    daily_sum INT NOT NULL,
    daily_count INT NOT NULL, 
    daily_average DOUBLE NOT NULL,
    created_time  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(user_id)
);