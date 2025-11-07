package com.zerogravity.myapp.chart.service;

import com.zerogravity.myapp.chart.dto.ChartCountResponse;
import com.zerogravity.myapp.chart.dto.ChartLevelResponse;
import com.zerogravity.myapp.chart.dto.ChartReasonResponse;

import java.time.LocalDate;
import java.time.ZoneId;

/**
 * Service interface for chart statistics
 * Provides real-time aggregated emotion statistics
 */
public interface ChartService {

	/**
	 * Get emotion level statistics for a period
	 *
	 * @param userId User ID
	 * @param period Period type (week/month/year)
	 * @param startDate Start date in user's timezone
	 * @param timezone User's timezone
	 * @return Chart level response with data and average
	 */
	ChartLevelResponse getEmotionLevelChart(Long userId, String period, LocalDate startDate, ZoneId timezone);

	/**
	 * Get emotion reason statistics for a period
	 *
	 * @param userId User ID
	 * @param period Period type (week/month/year)
	 * @param startDate Start date in user's timezone
	 * @param timezone User's timezone
	 * @return Chart reason response with data
	 */
	ChartReasonResponse getEmotionReasonChart(Long userId, String period, LocalDate startDate, ZoneId timezone);

	/**
	 * Get emotion count statistics for scatter chart
	 *
	 * @param userId User ID
	 * @param period Period type (week/month/year)
	 * @param startDate Start date in user's timezone
	 * @param timezone User's timezone
	 * @return Chart count response with data
	 */
	ChartCountResponse getEmotionCountChart(Long userId, String period, LocalDate startDate, ZoneId timezone);
}
