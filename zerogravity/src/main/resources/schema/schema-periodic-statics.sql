DROP DATABASE IF EXISTS zerogravity;
CREATE DATABASE zerogravity DEFAULT CHARACTER SET utf8mb4;

USE zerogravity;

CREATE TABLE periodic_statics (
	periodic_statics_id VARCHAR(36) PRIMARY KEY,
    user_id BIGINT,
    period_end DATETIME,
    period_type VARCHAR(10),
    sum_score INT,
    average_score DOUBLE,
    created_time DATETIME,
    updated_time DATETIME,
    FOREIGN KEY (user_id) REFERENCES user(user_id)
);