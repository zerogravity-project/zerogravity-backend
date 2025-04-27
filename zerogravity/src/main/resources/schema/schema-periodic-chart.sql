DROP DATABASE IF EXISTS zerogravity;
CREATE DATABASE zerogravity DEFAULT CHARACTER SET utf8mb4;

USE zerogravity;

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