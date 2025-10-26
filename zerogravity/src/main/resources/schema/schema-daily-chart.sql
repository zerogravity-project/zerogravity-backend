# Daily emotion statistics
# Aggregated emotion data for each day per user
# daily_sum: Sum of emotion levels
# daily_count: Number of emotion records
# daily_average: Average emotion level

CREATE TABLE daily_chart (
    daily_chart_id VARCHAR(36) NOT NULL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    daily_sum INT NOT NULL,
    daily_count INT NOT NULL,
    daily_average DOUBLE NOT NULL,
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(user_id)
);