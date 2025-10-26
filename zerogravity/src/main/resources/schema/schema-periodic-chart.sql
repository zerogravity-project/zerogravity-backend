# Periodic emotion statistics (weekly, monthly, yearly)
# Aggregated emotion data for periods
# period_type: 'weekly', 'monthly', or 'yearly'
# period_sum: Sum of emotion levels in the period
# period_count: Number of emotion records in the period
# period_average: Average emotion level in the period

CREATE TABLE periodic_chart (
    periodic_chart_id VARCHAR(36) NOT NULL PRIMARY KEY,
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