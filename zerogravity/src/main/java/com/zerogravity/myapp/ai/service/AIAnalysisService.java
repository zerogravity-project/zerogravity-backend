package com.zerogravity.myapp.ai.service;

import com.zerogravity.myapp.ai.dto.AIAnalysisResponse;
import com.zerogravity.myapp.ai.dto.DiarySummaryResponse;
import java.time.ZoneId;

/**
 * Service interface for AI emotion analysis
 * Handles analysis request coordination and caching
 */
public interface AIAnalysisService {

	/**
	 * Get AI emotion analysis summary for a period
	 * Returns cached result if available and valid
	 * Otherwise generates new analysis via Gemini API and caches result
	 *
	 * @param userId User ID
	 * @param period Period type (week/month/year)
	 * @param startDateStr Start date (YYYY-MM-DD)
	 * @param timezone User's timezone
	 * @return AI analysis response with summary
	 */
	AIAnalysisResponse getAnalysis(Long userId, String period, String startDateStr, ZoneId timezone);

	/**
	 * Get diary summary from emotion records in a period
	 * Requires at least 3 diary entries to generate summary
	 * Results are cached for 24 hours
	 *
	 * @param userId User ID
	 * @param startDateStr Start date (YYYY-MM-DD)
	 * @param endDateStr End date (YYYY-MM-DD)
	 * @param timezone User's timezone
	 * @return Diary summary response with AI-generated summary
	 */
	DiarySummaryResponse getDiarySummary(Long userId, String startDateStr, String endDateStr, ZoneId timezone);
}
