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
		List<Map<String, Object>> stats = emotionRecordDao.selectEmotionLevelStatsByPeriod(userId, periodStart, periodEnd, groupBy);

		// Build response
		Map<String, Double> dataMap = new HashMap<>();
		double totalLevel = 0.0;
		int totalCount = 0;

		for (Map<String, Object> row : stats) {
			String label = convertLabel(row.get("label"), period, timezone);
			Double avgLevel = (Double) row.get("avgLevel");
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

		// Query aggregated data
		List<Map<String, Object>> stats = emotionRecordDao.selectEmotionCountStatsByPeriod(userId, periodStart, periodEnd, groupBy);

		// Build response
		List<ChartCountResponse.DataPoint> dataPoints = stats.stream().map(row -> {
			Object timestampObj = row.get("timestamp");
			Instant timestamp = parseTimestamp(timestampObj);
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
				Instant instant = parseTimestamp(labelObj);
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
				// Day of month (1-31) + hour fraction
				return (double) zdt.getDayOfMonth() + zdt.getHour() / 24.0;
			case "year":
				// Month (1-12)
				return (double) zdt.getMonthValue();
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

	private Instant parseTimestamp(Object obj) {
		if (obj instanceof java.sql.Timestamp) {
			return ((java.sql.Timestamp) obj).toInstant();
		} else if (obj instanceof java.util.Date) {
			return ((java.util.Date) obj).toInstant();
		} else if (obj instanceof String) {
			return Instant.parse((String) obj);
		}
		throw new IllegalArgumentException("Cannot parse timestamp: " + obj);
	}

	private LocalDate parseDate(Object obj) {
		if (obj instanceof java.sql.Date) {
			return ((java.sql.Date) obj).toLocalDate();
		} else if (obj instanceof String) {
			return LocalDate.parse((String) obj);
		}
		throw new IllegalArgumentException("Cannot parse date: " + obj);
	}
}
