package com.zerogravity.myapp.chart.service;

import com.zerogravity.myapp.emotion.dao.EmotionDao;
import com.zerogravity.myapp.emotion.dao.EmotionRecordDao;
import com.zerogravity.myapp.emotion.dto.EmotionRecord;
import com.zerogravity.myapp.chart.dto.*;
import com.zerogravity.myapp.common.util.TimezoneUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ChartServiceImpl implements ChartService {

	private final EmotionRecordDao emotionRecordDao;
	private final EmotionDao emotionDao;

	public ChartServiceImpl(EmotionRecordDao emotionRecordDao, EmotionDao emotionDao) {
		this.emotionRecordDao = emotionRecordDao;
		this.emotionDao = emotionDao;
	}

	@Override
	@Transactional(readOnly = true)
	public ChartLevelResponse getEmotionLevelChart(Long userId, String period, LocalDate startDate, ZoneId timezone) {
		// Determine period range
		Instant periodStart, periodEnd;
		String groupBy;
		List<String> labels;

		switch (period.toLowerCase()) {
			case "week":
				periodStart = TimezoneUtil.getStartOfWeek(startDate, timezone);
				periodEnd = TimezoneUtil.getEndOfWeek(startDate, timezone);
				groupBy = "HOUR";
				labels = Arrays.asList("SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT");
				break;
			case "month":
				periodStart = TimezoneUtil.getStartOfMonth(startDate, timezone);
				periodEnd = TimezoneUtil.getEndOfMonth(startDate, timezone);
				groupBy = "DAY";
				labels = generateDayLabels(startDate);
				break;
			case "year":
				periodStart = TimezoneUtil.getStartOfYear(startDate, timezone);
				periodEnd = TimezoneUtil.getEndOfYear(startDate, timezone);
				groupBy = "MONTH";
				labels = Arrays.asList("JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC");
				break;
			default:
				throw new IllegalArgumentException("Invalid period: " + period);
		}

		// Query aggregated data
		String timezoneOffset = TimezoneUtil.getTimezoneOffset(timezone);
		List<Map<String, Object>> stats = emotionRecordDao.selectEmotionLevelStatsByPeriod(userId, periodStart, periodEnd, groupBy, timezoneOffset);

		// Build response
		Map<String, Double> dataMap = new HashMap<>();
		double totalLevel = 0.0;
		int totalCount = 0;

		for (Map<String, Object> row : stats) {
			String label = convertLabel(row.get("label"), period, timezone);
			Double avgLevel = ((Number) row.get("avgLevel")).doubleValue();
			Integer count = ((Number) row.get("count")).intValue();

			dataMap.put(label, avgLevel);
			totalLevel += avgLevel * count;
			totalCount += count;
		}

		// Fill missing labels with null or 0
		List<ChartLevelResponse.DataPoint> dataPoints = labels.stream()
			.map(label -> new ChartLevelResponse.DataPoint(label, dataMap.getOrDefault(label, null)))
			.collect(Collectors.toList());

		Double average = totalCount > 0 ? totalLevel / totalCount : null;

		return new ChartLevelResponse(dataPoints, average);
	}

	@Override
	@Transactional(readOnly = true)
	public ChartReasonResponse getEmotionReasonChart(Long userId, String period, LocalDate startDate, ZoneId timezone) {
		// Determine period range
		Instant periodStart, periodEnd;

		switch (period.toLowerCase()) {
			case "week":
				periodStart = TimezoneUtil.getStartOfWeek(startDate, timezone);
				periodEnd = TimezoneUtil.getEndOfWeek(startDate, timezone);
				break;
			case "month":
				periodStart = TimezoneUtil.getStartOfMonth(startDate, timezone);
				periodEnd = TimezoneUtil.getEndOfMonth(startDate, timezone);
				break;
			case "year":
				periodStart = TimezoneUtil.getStartOfYear(startDate, timezone);
				periodEnd = TimezoneUtil.getEndOfYear(startDate, timezone);
				break;
			default:
				throw new IllegalArgumentException("Invalid period: " + period);
		}

		// Query aggregated data
		List<Map<String, Object>> stats = emotionRecordDao.selectEmotionReasonStatsByPeriod(userId, periodStart, periodEnd);

		// Build response with ALL reasons (including zeros)
		Map<String, Integer> countMap = new HashMap<>();
		for (Map<String, Object> row : stats) {
			String reason = (String) row.get("reason");
			Integer count = ((Number) row.get("count")).intValue();
			countMap.put(reason, count);
		}

		// Include all 19 predefined reasons
		List<ChartReasonResponse.DataPoint> dataPoints = Arrays.stream(EmotionRecord.Reason.values())
			.map(reason -> new ChartReasonResponse.DataPoint(
				reason.getDisplayName(),
				countMap.getOrDefault(reason.getDisplayName(), 0)
			))
			.collect(Collectors.toList());

		return new ChartReasonResponse(dataPoints);
	}

	@Override
	@Transactional(readOnly = true)
	public ChartCountResponse getEmotionCountChart(Long userId, String period, LocalDate startDate, ZoneId timezone) {
		// Determine period range
		Instant periodStart, periodEnd;
		String groupBy;

		switch (period.toLowerCase()) {
			case "week":
				periodStart = TimezoneUtil.getStartOfWeek(startDate, timezone);
				periodEnd = TimezoneUtil.getEndOfWeek(startDate, timezone);
				groupBy = "HOUR";
				break;
			case "month":
				periodStart = TimezoneUtil.getStartOfMonth(startDate, timezone);
				periodEnd = TimezoneUtil.getEndOfMonth(startDate, timezone);
				groupBy = "DAY";
				break;
			case "year":
				periodStart = TimezoneUtil.getStartOfYear(startDate, timezone);
				periodEnd = TimezoneUtil.getEndOfYear(startDate, timezone);
				groupBy = "MONTH";
				break;
			default:
				throw new IllegalArgumentException("Invalid period: " + period);
		}

		// Query aggregated data (timestamps already converted to user timezone in SQL)
		String timezoneOffset = TimezoneUtil.getTimezoneOffset(timezone);
		List<Map<String, Object>> stats = emotionRecordDao.selectEmotionCountStatsByPeriod(userId, periodStart, periodEnd, groupBy, timezoneOffset);

		// Build response
		List<ChartCountResponse.DataPoint> dataPoints = stats.stream().map(row -> {
			Object timestampObj = row.get("timestamp");
			Instant timestamp = parseTimestamp(timestampObj, timezone);
			String label = convertLabel(timestampObj, period, timezone);
			Double position = calculatePosition(timestamp, period, startDate, timezone);

			Integer emotionId = (Integer) row.get("emotionId");
			String emotionType = (String) row.get("emotionType");
			Integer dailyCount = ((Number) row.get("dailyCount")).intValue();
			Integer momentCount = ((Number) row.get("momentCount")).intValue();
			Integer totalCount = ((Number) row.get("totalCount")).intValue();

			String timestampStr = TimezoneUtil.formatToUserTimezone(timestamp, timezone);

			return new ChartCountResponse.DataPoint(
				label, position, emotionId, emotionType, timestampStr,
				dailyCount, momentCount, totalCount
			);
		}).collect(Collectors.toList());

		return new ChartCountResponse(dataPoints);
	}

	// Helper methods

	private String convertLabel(Object labelObj, String period, ZoneId timezone) {
		if (labelObj == null) return "";

		switch (period.toLowerCase()) {
			case "week":
				// labelObj is timestamp, extract day of week
				Instant instant = parseTimestamp(labelObj, timezone);
				DayOfWeek dayOfWeek = instant.atZone(timezone).getDayOfWeek();
				return dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ENGLISH).toUpperCase();
			case "month":
				// labelObj is date, extract day number
				LocalDate date = parseDate(labelObj);
				return String.valueOf(date.getDayOfMonth());
			case "year":
				// labelObj is date, extract month
				LocalDate yearDate = parseDate(labelObj);
				return yearDate.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH).toUpperCase();
			default:
				return labelObj.toString();
		}
	}

	private Double calculatePosition(Instant timestamp, String period, LocalDate startDate, ZoneId timezone) {
		ZonedDateTime zdt = timestamp.atZone(timezone);

		switch (period.toLowerCase()) {
			case "week":
				// Day of week (0-6) + hour fraction
				int dayOfWeek = zdt.getDayOfWeek().getValue() % 7; // Sunday = 0
				double hourFraction = zdt.getHour() / 24.0;
				return dayOfWeek + hourFraction;
			case "month":
				// Day of month (0-30) + hour fraction
				return (double) (zdt.getDayOfMonth() - 1) + zdt.getHour() / 24.0;
			case "year":
				// Month (0-11)
				return (double) (zdt.getMonthValue() - 1);
			default:
				return 0.0;
		}
	}

	private List<String> generateDayLabels(LocalDate date) {
		int daysInMonth = date.lengthOfMonth();
		List<String> labels = new ArrayList<>();
		for (int i = 1; i <= daysInMonth; i++) {
			labels.add(String.valueOf(i));
		}
		return labels;
	}

	/**
	 * Parse timestamp that was already converted to user timezone in SQL (via CONVERT_TZ)
	 * The timestamp string from SQL represents user's local time, so we need to interpret it
	 * in the user's timezone to get the correct Instant.
	 */
	private Instant parseTimestamp(Object obj, ZoneId timezone) {
		if (obj instanceof java.sql.Timestamp) {
			// Timestamp from SQL CONVERT_TZ - interpret as user timezone
			LocalDateTime ldt = ((java.sql.Timestamp) obj).toLocalDateTime();
			return ldt.atZone(timezone).toInstant();
		} else if (obj instanceof java.util.Date) {
			return ((java.util.Date) obj).toInstant();
		} else if (obj instanceof String) {
			String timestampStr = (String) obj;
			// Handle "YYYY-MM-DD HH:MM:SS" format from MySQL DATE_FORMAT() after CONVERT_TZ
			// This represents user's local time, not UTC
			if (timestampStr.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}")) {
				// Parse as LocalDateTime, then interpret in user timezone
				LocalDateTime ldt = LocalDateTime.parse(timestampStr.replace(" ", "T"));
				return ldt.atZone(timezone).toInstant();
			}
			// Fallback for ISO format
			return Instant.parse(timestampStr);
		}
		throw new IllegalArgumentException("Cannot parse timestamp: " + obj);
	}

	private LocalDate parseDate(Object obj) {
		if (obj instanceof java.sql.Date) {
			return ((java.sql.Date) obj).toLocalDate();
		} else if (obj instanceof String) {
			String dateStr = (String) obj;
			// Handle both "YYYY-MM-DD" and "YYYY-MM-DD HH:MM:SS" formats
			if (dateStr.contains(" ")) {
				// Extract date part from "YYYY-MM-DD HH:MM:SS"
				dateStr = dateStr.substring(0, 10);
			}
			return LocalDate.parse(dateStr);
		}
		throw new IllegalArgumentException("Cannot parse date: " + obj);
	}
}
