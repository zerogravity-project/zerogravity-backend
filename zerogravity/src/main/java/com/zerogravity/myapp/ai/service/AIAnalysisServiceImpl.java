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
import java.util.*;
import java.util.stream.Collectors;

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
	private final com.zerogravity.myapp.emotion.dao.EmotionDao emotionDao;
	private final SnowflakeIdService snowflakeIdService;
	private final ObjectMapper objectMapper;

	public AIAnalysisServiceImpl(AIAnalysisCacheDao cacheDao,
							   GeminiService geminiService,
							   ChartService chartService,
							   EmotionRecordDao emotionRecordDao,
							   com.zerogravity.myapp.emotion.dao.EmotionDao emotionDao,
							   SnowflakeIdService snowflakeIdService) {
		this.cacheDao = cacheDao;
		this.geminiService = geminiService;
		this.chartService = chartService;
		this.emotionRecordDao = emotionRecordDao;
		this.emotionDao = emotionDao;
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
			List<EmotionRecord> emotionRecords = emotionRecordDao.selectEmotionRecordByPeriodAndUserId(userId, periodStart, periodEnd);

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

			// 4.5. Select representative records using statistical sampling
			List<EmotionRecord> representativeRecords = selectRepresentativeRecords(
				emotionRecords,
				levelChart,
				period,
				startDate,
				timezone
			);

			// 5. Generate AI summary with representative records and timezone
			SummaryData summaryData = geminiService.generateSummary(
				period,
				startDate.toString(),
				endDate.toString(),
				levelChart,
				reasonChart,
				countChart,
				representativeRecords,  // Use representative records instead of all
				timezone  // Pass timezone for ISO 8601 formatting
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
	 * Select representative emotion records using statistical sampling
	 * Chooses records that best match each time bucket's average level and top reasons
	 *
	 * @param allRecords All emotion records in the period
	 * @param levelChart Chart with level statistics (contains bucket averages)
	 * @param period Period type (week/month/year)
	 * @param startDate Period start date
	 * @param timezone User timezone
	 * @return List of representative records (7 for week, 4 for month, 12 for year)
	 */
	private List<EmotionRecord> selectRepresentativeRecords(
			List<EmotionRecord> allRecords,
			ChartLevelResponse levelChart,
			String period,
			LocalDate startDate,
			ZoneId timezone) {

		if (allRecords == null || allRecords.isEmpty()) {
			return java.util.Collections.emptyList();
		}

		// Determine groupBy strategy based on period
		String groupBy;
		switch (period.toLowerCase()) {
			case "week":
				groupBy = "DAY";  // HOUR data → group by DAY
				break;
			case "month":
				groupBy = "DAY";  // DAY data → regroup by WEEK
				break;
			case "year":
				groupBy = "MONTH"; // MONTH data → use as-is
				break;
			default:
				throw new IllegalArgumentException("Invalid period: " + period);
		}

		// Get bucket-wise reason statistics
		Instant periodStart = calculatePeriodStart(startDate, period, timezone);
		Instant periodEnd = calculatePeriodEnd(calculateEndDate(startDate, period, timezone), period, timezone);
		String timezoneOffset = TimezoneUtil.getTimezoneOffset(timezone);

		List<Map<String, Object>> bucketReasonStats = emotionRecordDao.selectEmotionReasonStatsByPeriod(
				allRecords.get(0).getUserId(), periodStart, periodEnd, groupBy, timezoneOffset
		);

		// Build bucket top reasons map
		Map<String, String> bucketTopReasons = buildBucketTopReasonsMap(bucketReasonStats);

		// Select representative record for each bucket
		List<EmotionRecord> representatives = new ArrayList<>();

		if ("year".equals(period.toLowerCase())) {
			// Year: 12 months, use MONTH buckets directly
			representatives = selectByMonthBuckets(allRecords, levelChart, bucketTopReasons, timezone);
		} else if ("week".equals(period.toLowerCase())) {
			// Week: 7 days, use DAY buckets directly
			representatives = selectByDayBuckets(allRecords, levelChart, bucketTopReasons, timezone);
		} else if ("month".equals(period.toLowerCase())) {
			// Month: 4 weeks, regroup DAY data into weekly buckets
			representatives = selectByWeeklyBuckets(allRecords, levelChart, bucketTopReasons, startDate, timezone);
		}

		return representatives;
	}

	/**
	 * Build map of bucket -> top reason from bucket reason statistics
	 */
	private Map<String, String> buildBucketTopReasonsMap(List<Map<String, Object>> bucketReasonStats) {
		Map<String, String> bucketTopReasons = new HashMap<>();
		Map<String, Map<String, Integer>> bucketReasonCounts = new HashMap<>();

		// Group by bucket and find top reason per bucket
		for (Map<String, Object> stat : bucketReasonStats) {
			String bucket = (String) stat.get("bucket");
			String reason = (String) stat.get("reason");
			Integer count = ((Number) stat.get("count")).intValue();

			bucketReasonCounts.computeIfAbsent(bucket, k -> new HashMap<>()).put(reason, count);
		}

		// Find top reason for each bucket
		for (Map.Entry<String, Map<String, Integer>> entry : bucketReasonCounts.entrySet()) {
			String bucket = entry.getKey();
			String topReason = entry.getValue().entrySet().stream()
					.max(Map.Entry.comparingByValue())
					.map(Map.Entry::getKey)
					.orElse(null);
			bucketTopReasons.put(bucket, topReason);
		}

		return bucketTopReasons;
	}

	/**
	 * Select representative records using monthly buckets (for year period)
	 */
	private List<EmotionRecord> selectByMonthBuckets(
			List<EmotionRecord> allRecords,
			ChartLevelResponse levelChart,
			Map<String, String> bucketTopReasons,
			ZoneId timezone) {

		List<EmotionRecord> representatives = new ArrayList<>();

		for (ChartLevelResponse.DataPoint dataPoint : levelChart.getData()) {
			if (dataPoint.getValue() == null) continue;

			String monthLabel = dataPoint.getLabel(); // "JAN", "FEB", etc.
			Double targetLevel = dataPoint.getValue();

			// Convert label to month number (1-12)
			int monthNumber = parseMonthAbbreviation(monthLabel);

			// Find bucket key (format: "YYYY-MM-01 00:00:00")
			String bucketKey = findBucketKeyForMonth(bucketTopReasons.keySet(), monthNumber);
			String topReason = bucketTopReasons.get(bucketKey);

			// Filter records in this month
			List<EmotionRecord> monthRecords = allRecords.stream()
					.filter(r -> r.getCreatedTime().toInstant().atZone(timezone).getMonthValue() == monthNumber)
					.collect(Collectors.toList());

			// Find best matching record
			EmotionRecord best = findBestMatchingRecord(monthRecords, targetLevel, topReason);
			if (best != null) {
				representatives.add(best);
			}
		}

		return representatives;
	}

	/**
	 * Select representative records using daily buckets (for week period)
	 */
	private List<EmotionRecord> selectByDayBuckets(
			List<EmotionRecord> allRecords,
			ChartLevelResponse levelChart,
			Map<String, String> bucketTopReasons,
			ZoneId timezone) {

		List<EmotionRecord> representatives = new ArrayList<>();

		// Group HOUR-level data by DAY
		Map<String, List<ChartLevelResponse.DataPoint>> dayData = new HashMap<>();
		for (ChartLevelResponse.DataPoint dataPoint : levelChart.getData()) {
			if (dataPoint.getValue() == null) continue;
			String dayLabel = dataPoint.getLabel(); // "SUN", "MON", etc.
			dayData.computeIfAbsent(dayLabel, k -> new ArrayList<>()).add(dataPoint);
		}

		// Process each day
		String[] daysOfWeek = {"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};
		for (String dayLabel : daysOfWeek) {
			List<ChartLevelResponse.DataPoint> dayPoints = dayData.get(dayLabel);
			if (dayPoints == null || dayPoints.isEmpty()) continue;

			// Calculate day average from hourly data
			Double targetLevel = dayPoints.stream()
					.mapToDouble(ChartLevelResponse.DataPoint::getValue)
					.average()
					.orElse(0.0);

			// Find bucket key for this day
			String bucketKey = findBucketKeyForDay(bucketTopReasons.keySet(), dayLabel);
			String topReason = bucketTopReasons.get(bucketKey);

			// Filter records for this day
			List<EmotionRecord> dayRecords = allRecords.stream()
					.filter(r -> {
						String recordDay = r.getCreatedTime().toInstant()
								.atZone(timezone)
								.getDayOfWeek()
								.getDisplayName(java.time.format.TextStyle.SHORT, java.util.Locale.ENGLISH)
								.toUpperCase();
						return recordDay.equals(dayLabel);
					})
					.collect(Collectors.toList());

			// Find best matching record
			EmotionRecord best = findBestMatchingRecord(dayRecords, targetLevel, topReason);
			if (best != null) {
				representatives.add(best);
			}
		}

		return representatives;
	}

	/**
	 * Select representative records by regrouping daily data into 4 weekly buckets (for month period)
	 */
	private List<EmotionRecord> selectByWeeklyBuckets(
			List<EmotionRecord> allRecords,
			ChartLevelResponse levelChart,
			Map<String, String> bucketTopReasons,
			LocalDate startDate,
			ZoneId timezone) {

		List<EmotionRecord> representatives = new ArrayList<>();

		// Define 4 weekly buckets
		int daysInMonth = startDate.lengthOfMonth();
		int[][] weekRanges = {
				{1, 7},
				{8, 14},
				{15, 21},
				{22, daysInMonth}
		};

		for (int[] range : weekRanges) {
			int startDay = range[0];
			int endDay = range[1];

			// Calculate average level for this week from daily data
			Double targetLevel = levelChart.getData().stream()
					.filter(dp -> {
						try {
							int day = Integer.parseInt(dp.getLabel());
							return day >= startDay && day <= endDay && dp.getValue() != null;
						} catch (NumberFormatException e) {
							return false;
						}
					})
					.mapToDouble(ChartLevelResponse.DataPoint::getValue)
					.average()
					.orElse(0.0);

			// Find top reason for this week from bucket data
			String topReason = findTopReasonForWeek(bucketTopReasons, startDay, endDay);

			// Filter records in this week
			List<EmotionRecord> weekRecords = allRecords.stream()
					.filter(r -> {
						int day = r.getCreatedTime().toInstant().atZone(timezone).getDayOfMonth();
						return day >= startDay && day <= endDay;
					})
					.collect(Collectors.toList());

			// Find best matching record
			EmotionRecord best = findBestMatchingRecord(weekRecords, targetLevel, topReason);
			if (best != null) {
				representatives.add(best);
			}
		}

		return representatives;
	}

	/**
	 * Find the best matching record based on level and reason matching
	 * Applies Daily record 1.5x weighting to level comparison
	 */
	private EmotionRecord findBestMatchingRecord(
			List<EmotionRecord> records,
			Double targetLevel,
			String topReason) {

		if (records == null || records.isEmpty() || targetLevel == null) {
			return null;
		}

		return records.stream()
				.max(Comparator
						// 1st: Total score (level + reason matching)
						.<EmotionRecord>comparingDouble(r -> calculateMatchScore(r, targetLevel, topReason))
						// 2nd: Diary length (more context for AI)
						.thenComparingInt(r -> r.getDiaryEntry() != null ? r.getDiaryEntry().length() : 0)
						// 3rd: Reason count (more context)
						.thenComparingInt(r -> r.getEmotionReasons() != null ? r.getEmotionReasons().size() : 0)
						// 4th: Most recent
						.thenComparing(EmotionRecord::getCreatedTime))
				.orElse(null);
	}

	/**
	 * Calculate match score for a record
	 * Applies Daily 1.5x weighting to level comparison
	 */
	private double calculateMatchScore(EmotionRecord record, Double targetLevel, String topReason) {
		// Level score with Daily weighting
		double recordLevel = record.getEmotionId() *
				(record.getEmotionRecordType() == EmotionRecord.Type.DAILY ? 1.5 : 1.0);
		double levelDiff = Math.abs(recordLevel - targetLevel);
		double levelScore = 1.0 - (levelDiff / 9.0); // Max diff = 6 * 1.5 = 9
		levelScore = Math.max(0.0, Math.min(1.0, levelScore)); // Clamp to [0, 1]

		// Reason score
		double reasonScore = 0.0;
		if (topReason != null && record.getEmotionReasons() != null) {
			reasonScore = record.getEmotionReasons().contains(topReason) ? 1.0 : 0.0;
		}

		// Total score: 60% level, 40% reason
		return (levelScore * 0.6) + (reasonScore * 0.4);
	}

	/**
	 * Helper: Find bucket key for a specific month number
	 */
	private String findBucketKeyForMonth(Set<String> bucketKeys, int monthNumber) {
		return bucketKeys.stream()
				.filter(key -> key.contains(String.format("-%02d-", monthNumber)))
				.findFirst()
				.orElse(null);
	}

	/**
	 * Helper: Find bucket key for a specific day of week
	 */
	private String findBucketKeyForDay(Set<String> bucketKeys, String dayLabel) {
		// Bucket keys are dates like "2025-01-15 00:00:00"
		// We need to find one that matches the day of week
		return bucketKeys.stream()
				.filter(key -> {
					try {
						LocalDate date = LocalDate.parse(key.substring(0, 10));
						String keyDay = date.getDayOfWeek()
								.getDisplayName(java.time.format.TextStyle.SHORT, java.util.Locale.ENGLISH)
								.toUpperCase();
						return keyDay.equals(dayLabel);
					} catch (Exception e) {
						return false;
					}
				})
				.findFirst()
				.orElse(null);
	}

	/**
	 * Helper: Find top reason for a weekly range from bucket data
	 */
	private String findTopReasonForWeek(Map<String, String> bucketTopReasons, int startDay, int endDay) {
		// Find all bucket keys within this day range
		Map<String, Integer> reasonCounts = new HashMap<>();

		for (Map.Entry<String, String> entry : bucketTopReasons.entrySet()) {
			try {
				String key = entry.getKey();
				int day = Integer.parseInt(key.substring(8, 10)); // Extract day from "YYYY-MM-DD ..."
				if (day >= startDay && day <= endDay && entry.getValue() != null) {
					reasonCounts.merge(entry.getValue(), 1, Integer::sum);
				}
			} catch (Exception e) {
				// Skip invalid keys
			}
		}

		return reasonCounts.entrySet().stream()
				.max(Map.Entry.comparingByValue())
				.map(Map.Entry::getKey)
				.orElse(null);
	}

	/**
	 * Parse 3-letter month abbreviation to month number (1-12)
	 * Supports: JAN, FEB, MAR, APR, MAY, JUN, JUL, AUG, SEP, OCT, NOV, DEC
	 */
	private int parseMonthAbbreviation(String monthLabel) {
		switch (monthLabel.toUpperCase()) {
			case "JAN": return 1;
			case "FEB": return 2;
			case "MAR": return 3;
			case "APR": return 4;
			case "MAY": return 5;
			case "JUN": return 6;
			case "JUL": return 7;
			case "AUG": return 8;
			case "SEP": return 9;
			case "OCT": return 10;
			case "NOV": return 11;
			case "DEC": return 12;
			default:
				throw new IllegalArgumentException("Invalid month abbreviation: " + monthLabel);
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
			List<EmotionRecord> records = emotionRecordDao.selectEmotionRecordByPeriodAndUserId(userId, periodStart, periodEnd);

			// Filter records that have diary entries and add date prefix
			List<String> diaryEntries = records.stream()
				.filter(r -> r.getDiaryEntry() != null && !r.getDiaryEntry().trim().isEmpty())
				.map(r -> {
					// Format date in user timezone
					String dateStr = TimezoneUtil.formatToUserTimezone(r.getCreatedTime().toInstant(), timezone);
					// Extract just the date part (YYYY-MM-DD)
					String datePart = dateStr.substring(0, 10);
					return "[" + datePart + "] " + r.getDiaryEntry();
				})
				.toList();

			// Check if we have enough entries
			if (diaryEntries.size() < 3) {
				throw new IllegalArgumentException("At least 3 diary entries are required to generate a summary. Found: " + diaryEntries.size());
			}

			// Generate summary using Gemini (max 10000 characters)
			String summary = geminiService.summarizeDiaries(diaryEntries, 10000);

			return new DiarySummaryResponse(summary, diaryEntries.size());
		} catch (IllegalArgumentException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException("Failed to get diary summary: " + e.getMessage(), e);
		}
	}
}
