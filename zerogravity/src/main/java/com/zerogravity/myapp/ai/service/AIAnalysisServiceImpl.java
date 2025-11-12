package com.zerogravity.myapp.ai.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerogravity.myapp.ai.dao.AIAnalysisCacheDao;
import com.zerogravity.myapp.ai.dto.AIAnalysisCache;
import com.zerogravity.myapp.ai.dto.AIAnalysisResponse;
import com.zerogravity.myapp.ai.dto.DiarySummaryResponse;
import com.zerogravity.myapp.ai.dto.SummaryData;
import com.zerogravity.myapp.ai.exception.AIAnalysisCacheException;
import com.zerogravity.myapp.chart.dto.ChartCountResponse;
import com.zerogravity.myapp.chart.dto.ChartLevelResponse;
import com.zerogravity.myapp.chart.dto.ChartReasonResponse;
import com.zerogravity.myapp.chart.service.ChartService;
import com.zerogravity.myapp.common.security.SnowflakeIdService;
import com.zerogravity.myapp.common.util.TimezoneUtil;
import com.zerogravity.myapp.emotion.dao.EmotionRecordDao;
import com.zerogravity.myapp.emotion.dto.EmotionRecord;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.sql.Timestamp;
import java.util.List;

/**
 * Implementation of AIAnalysisService
 * Coordinates cache, data retrieval, and AI summary generation
 */
@Service
public class AIAnalysisServiceImpl implements AIAnalysisService {

	private final AIAnalysisCacheDao cacheDao;
	private final GeminiService geminiService;
	private final ChartService chartService;
	private final EmotionRecordDao emotionRecordDao;
	private final SnowflakeIdService snowflakeIdService;
	private final ObjectMapper objectMapper;

	public AIAnalysisServiceImpl(AIAnalysisCacheDao cacheDao,
							   GeminiService geminiService,
							   ChartService chartService,
							   EmotionRecordDao emotionRecordDao,
							   SnowflakeIdService snowflakeIdService) {
		this.cacheDao = cacheDao;
		this.geminiService = geminiService;
		this.chartService = chartService;
		this.emotionRecordDao = emotionRecordDao;
		this.snowflakeIdService = snowflakeIdService;
		this.objectMapper = new ObjectMapper();
	}

	@Override
	@Transactional
	public AIAnalysisResponse getAnalysis(Long userId, String period, String startDateStr, ZoneId timezone) {
		try {
			LocalDate startDate = LocalDate.parse(startDateStr);

			// 1. Check cache
			AIAnalysisCache validCache = cacheDao.selectValidCache(userId, period.toUpperCase(), startDate);
			if (validCache != null) {
				// Cache hit - parse and return
				SummaryData summaryData = objectMapper.readValue(validCache.getSummaryJson(), SummaryData.class);
				return buildResponse(period, startDate, validCache.getEndDate(), summaryData,
					TimezoneUtil.formatToUserTimezone(validCache.getGeneratedAt().toInstant(), timezone));
			}

			// 2. Cache miss - fetch data
			LocalDate endDate = calculateEndDate(startDate, period, timezone);
			Instant periodStart = calculatePeriodStart(startDate, period, timezone);
			Instant periodEnd = calculatePeriodEnd(endDate, period, timezone);

			// Fetch emotion records
			String timezoneOffset = TimezoneUtil.getTimezoneOffset(timezone);
			List<EmotionRecord> emotionRecords = emotionRecordDao.selectEmotionRecordByPeriodAndUserId(userId, periodStart, periodEnd, timezoneOffset);

			// 3. Check if no emotion records exist
			if (emotionRecords.isEmpty()) {
				// No data - return default response without calling Gemini API
				SummaryData defaultSummary = new SummaryData(
					"해당 기간에 기록된 감정이 없습니다.",
					java.util.Arrays.asList(),
					java.util.Arrays.asList("감정 기록을 시작해보세요!")
				);
				return buildResponse(period, startDate, endDate, defaultSummary,
					TimezoneUtil.formatToUserTimezone(Instant.now(), timezone));
			}

			// 4. Fetch chart data
			ChartLevelResponse levelChart = chartService.getEmotionLevelChart(userId, period, startDate, timezone);
			ChartReasonResponse reasonChart = chartService.getEmotionReasonChart(userId, period, startDate, timezone);
			ChartCountResponse countChart = chartService.getEmotionCountChart(userId, period, startDate, timezone);

			// 5. Generate AI summary
			SummaryData summaryData = geminiService.generateSummary(
				period,
				startDate.toString(),
				endDate.toString(),
				levelChart,
				reasonChart,
				countChart,
				emotionRecords
			);

			// 6. Cache the result
			Instant now = Instant.now();
			Timestamp nowTimestamp = Timestamp.from(now);
			Timestamp expiresAtTimestamp = Timestamp.from(now.plusSeconds(24 * 60 * 60)); // 24 hours

			AIAnalysisCache cacheEntry = new AIAnalysisCache(
				snowflakeIdService.generateId(),
				userId,
				period.toUpperCase(),
				startDate,
				endDate,
				objectMapper.writeValueAsString(summaryData),
				nowTimestamp,
				expiresAtTimestamp
			);

			cacheDao.insertCache(cacheEntry);

			// 7. Build and return response
			return buildResponse(period, startDate, endDate, summaryData,
				TimezoneUtil.formatToUserTimezone(now, timezone));

		} catch (Exception e) {
			if (e instanceof com.zerogravity.myapp.ai.exception.GeminiApiException ||
				e instanceof AIAnalysisCacheException) {
				throw (RuntimeException) e;
			}
			throw new AIAnalysisCacheException("Failed to get AI analysis: " + e.getMessage(), e);
		}
	}

	/**
	 * Build AIAnalysisResponse from components
	 */
	private AIAnalysisResponse buildResponse(String period, LocalDate startDate, LocalDate endDate,
											 SummaryData summaryData, String generatedAt) {
		return new AIAnalysisResponse(
			period,
			startDate.toString(),
			endDate.toString(),
			summaryData,
			generatedAt
		);
	}

	/**
	 * Calculate end date based on period and start date
	 */
	private LocalDate calculateEndDate(LocalDate startDate, String period, ZoneId timezone) {
		switch (period.toLowerCase()) {
			case "week":
				return TimezoneUtil.getEndOfWeek(startDate, timezone).atZone(timezone).toLocalDate();
			case "month":
				return TimezoneUtil.getEndOfMonth(startDate, timezone).atZone(timezone).toLocalDate();
			case "year":
				return TimezoneUtil.getEndOfYear(startDate, timezone).atZone(timezone).toLocalDate();
			default:
				throw new IllegalArgumentException("Invalid period: " + period);
		}
	}

	/**
	 * Calculate period start instant in UTC
	 */
	private Instant calculatePeriodStart(LocalDate startDate, String period, ZoneId timezone) {
		switch (period.toLowerCase()) {
			case "week":
				return TimezoneUtil.getStartOfWeek(startDate, timezone);
			case "month":
				return TimezoneUtil.getStartOfMonth(startDate, timezone);
			case "year":
				return TimezoneUtil.getStartOfYear(startDate, timezone);
			default:
				throw new IllegalArgumentException("Invalid period: " + period);
		}
	}

	/**
	 * Calculate period end instant in UTC
	 */
	private Instant calculatePeriodEnd(LocalDate endDate, String period, ZoneId timezone) {
		switch (period.toLowerCase()) {
			case "week":
				return TimezoneUtil.getEndOfWeek(endDate, timezone);
			case "month":
				return TimezoneUtil.getEndOfMonth(endDate, timezone);
			case "year":
				return TimezoneUtil.getEndOfYear(endDate, timezone);
			default:
				throw new IllegalArgumentException("Invalid period: " + period);
		}
	}

	/**
	 * Get diary summary from emotion records in a period
	 * Requires at least 3 diary entries to generate summary
	 * Results are cached for 24 hours
	 */
	@Override
	public DiarySummaryResponse getDiarySummary(Long userId, String startDateStr, String endDateStr, ZoneId timezone) {
		try {
			// Parse dates
			LocalDate startDate = LocalDate.parse(startDateStr);
			LocalDate endDate = LocalDate.parse(endDateStr);

			// Convert to UTC instants
			Instant periodStart = startDate.atStartOfDay(timezone).toInstant();
			Instant periodEnd = endDate.plusDays(1).atStartOfDay(timezone).toInstant();

			// Get emotion records with diary entries
			String timezoneOffset = TimezoneUtil.getTimezoneOffset(timezone);
			List<EmotionRecord> records = emotionRecordDao.selectEmotionRecordByPeriodAndUserId(userId, periodStart, periodEnd, timezoneOffset);

			// Filter records that have diary entries
			List<String> diaryEntries = records.stream()
				.filter(r -> r.getDiaryEntry() != null && !r.getDiaryEntry().trim().isEmpty())
				.map(EmotionRecord::getDiaryEntry)
				.toList();

			// Check if we have enough entries
			if (diaryEntries.size() < 3) {
				throw new IllegalArgumentException("At least 3 diary entries are required to generate a summary. Found: " + diaryEntries.size());
			}

			// Generate summary using Gemini
			String summary = geminiService.summarizeDiaries(diaryEntries, 1000);

			return new DiarySummaryResponse(summary, diaryEntries.size());
		} catch (IllegalArgumentException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException("Failed to get diary summary: " + e.getMessage(), e);
		}
	}
}
