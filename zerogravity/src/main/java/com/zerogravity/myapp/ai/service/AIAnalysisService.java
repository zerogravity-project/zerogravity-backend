package com.zerogravity.myapp.ai.service;

import com.zerogravity.myapp.ai.dto.AIAnalysisResponse;
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
}
