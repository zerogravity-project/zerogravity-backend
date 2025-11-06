package com.zerogravity.myapp.ai.service;

import com.zerogravity.myapp.ai.dto.SummaryData;
import com.zerogravity.myapp.chart.dto.ChartCountResponse;
import com.zerogravity.myapp.chart.dto.ChartLevelResponse;
import com.zerogravity.myapp.chart.dto.ChartReasonResponse;
import com.zerogravity.myapp.emotion.dto.EmotionRecord;
import java.util.List;

/**
 * Service interface for Gemini AI integration
 * Handles AI-powered emotion analysis and prediction
 */
public interface GeminiService {

	/**
	 * Generate AI summary using Gemini API
	 * Takes emotion data and returns structured summary with insights and recommendations
	 *
	 * @param period Period type (week/month/year)
	 * @param startDateStr Start date as string (YYYY-MM-DD)
	 * @param endDateStr End date as string (YYYY-MM-DD)
	 * @param levelChart Emotion level statistics
	 * @param reasonChart Emotion reason statistics
	 * @param countChart Emotion count statistics
	 * @param emotionRecords Individual emotion records
	 * @return Generated summary data with overview, insights, and recommendations
	 */
	SummaryData generateSummary(String period,
							   String startDateStr,
							   String endDateStr,
							   ChartLevelResponse levelChart,
							   ChartReasonResponse reasonChart,
							   ChartCountResponse countChart,
							   List<EmotionRecord> emotionRecords);

	/**
	 * Predict emotion from diary entry
	 * AI predicts missing information (emotionId and/or emotionReasons)
	 *
	 * @param diaryEntry Diary entry text
	 * @param providedEmotionId User-provided emotion ID (null if not provided)
	 * @param providedReasons User-provided emotion reasons (null if not provided)
	 * @return Prediction result with suggested emotionId, reasons, reasoning, and confidence
	 */
	EmotionPredictionResult predictEmotion(String diaryEntry,
										  Integer providedEmotionId,
										  List<String> providedReasons);

	/**
	 * Summarize diary entries using Gemini API
	 * Generates a concise summary of multiple diary entries
	 *
	 * @param diaryEntries List of diary entry texts
	 * @param maxLength Maximum length of summary in characters
	 * @return Generated summary text (max length respected)
	 */
	String summarizeDiaries(List<String> diaryEntries, int maxLength);
}
