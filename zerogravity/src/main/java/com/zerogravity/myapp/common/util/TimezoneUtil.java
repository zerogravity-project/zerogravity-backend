package com.zerogravity.myapp.common.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

/**
 * Timezone Utility for handling timezone conversions
 *
 * Design Philosophy:
 * - Database: Always store in UTC (Instant, DATETIME in UTC)
 * - Client: Send timezone via X-Timezone header (e.g., "Asia/Seoul")
 * - Response: Convert UTC to client timezone with offset (ISO 8601 format)
 *
 * Example Flow:
 * 1. Client sends: "2025-10-26T15:00:00" + X-Timezone: "Asia/Seoul"
 * 2. Backend converts to UTC: 2025-10-26T06:00:00Z
 * 3. Store in DB: 2025-10-26 06:00:00 (UTC)
 * 4. Response: "2025-10-26T15:00:00+09:00" (Seoul time with offset)
 */
public class TimezoneUtil {

	private static final DateTimeFormatter ISO_OFFSET_FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

	/**
	 * Convert UTC Instant to user's timezone and format as ISO 8601 with offset
	 *
	 * @param utcInstant UTC instant
	 * @param timezone User's timezone
	 * @return ISO 8601 formatted string with offset (e.g., "2025-10-26T15:00:00+09:00")
	 */
	public static String formatToUserTimezone(Instant utcInstant, ZoneId timezone) {
		if (utcInstant == null) {
			return null;
		}
		ZonedDateTime userTime = utcInstant.atZone(timezone);
		return userTime.format(ISO_OFFSET_FORMATTER);
	}

	/**
	 * Parse user's datetime string in their timezone and convert to UTC Instant
	 *
	 * @param dateTimeStr DateTime string (e.g., "2025-10-26T15:00:00")
	 * @param timezone User's timezone
	 * @return UTC Instant
	 */
	public static Instant parseToUtc(String dateTimeStr, ZoneId timezone) {
		if (dateTimeStr == null) {
			return null;
		}
		LocalDateTime localDateTime = LocalDateTime.parse(dateTimeStr);
		ZonedDateTime zonedDateTime = localDateTime.atZone(timezone);
		return zonedDateTime.toInstant();
	}

	/**
	 * Get start of day in user's timezone as UTC Instant
	 *
	 * @param date Date in user's timezone
	 * @param timezone User's timezone
	 * @return UTC Instant representing start of day
	 */
	public static Instant getStartOfDay(LocalDate date, ZoneId timezone) {
		return date.atStartOfDay(timezone).toInstant();
	}

	/**
	 * Get end of day in user's timezone as UTC Instant
	 *
	 * @param date Date in user's timezone
	 * @param timezone User's timezone
	 * @return UTC Instant representing end of day (23:59:59)
	 */
	public static Instant getEndOfDay(LocalDate date, ZoneId timezone) {
		return date.atTime(23, 59, 59).atZone(timezone).toInstant();
	}

	/**
	 * Get start of week (Sunday) in user's timezone
	 *
	 * @param date Any date in the week
	 * @param timezone User's timezone
	 * @return UTC Instant representing start of week (Sunday 00:00:00)
	 */
	public static Instant getStartOfWeek(LocalDate date, ZoneId timezone) {
		LocalDate sunday = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
		return sunday.atStartOfDay(timezone).toInstant();
	}

	/**
	 * Get end of week (Saturday) in user's timezone
	 *
	 * @param date Any date in the week
	 * @param timezone User's timezone
	 * @return UTC Instant representing end of week (Saturday 23:59:59)
	 */
	public static Instant getEndOfWeek(LocalDate date, ZoneId timezone) {
		LocalDate saturday = date.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY));
		return saturday.atTime(23, 59, 59).atZone(timezone).toInstant();
	}

	/**
	 * Get start of month in user's timezone
	 *
	 * @param date Any date in the month
	 * @param timezone User's timezone
	 * @return UTC Instant representing start of month (1st day 00:00:00)
	 */
	public static Instant getStartOfMonth(LocalDate date, ZoneId timezone) {
		LocalDate firstDay = date.with(TemporalAdjusters.firstDayOfMonth());
		return firstDay.atStartOfDay(timezone).toInstant();
	}

	/**
	 * Get end of month in user's timezone
	 *
	 * @param date Any date in the month
	 * @param timezone User's timezone
	 * @return UTC Instant representing end of month (last day 23:59:59)
	 */
	public static Instant getEndOfMonth(LocalDate date, ZoneId timezone) {
		LocalDate lastDay = date.with(TemporalAdjusters.lastDayOfMonth());
		return lastDay.atTime(23, 59, 59).atZone(timezone).toInstant();
	}

	/**
	 * Get start of year in user's timezone
	 *
	 * @param date Any date in the year
	 * @param timezone User's timezone
	 * @return UTC Instant representing start of year (Jan 1st 00:00:00)
	 */
	public static Instant getStartOfYear(LocalDate date, ZoneId timezone) {
		LocalDate firstDay = date.with(TemporalAdjusters.firstDayOfYear());
		return firstDay.atStartOfDay(timezone).toInstant();
	}

	/**
	 * Get end of year in user's timezone
	 *
	 * @param date Any date in the year
	 * @param timezone User's timezone
	 * @return UTC Instant representing end of year (Dec 31st 23:59:59)
	 */
	public static Instant getEndOfYear(LocalDate date, ZoneId timezone) {
		LocalDate lastDay = date.with(TemporalAdjusters.lastDayOfYear());
		return lastDay.atTime(23, 59, 59).atZone(timezone).toInstant();
	}

	/**
	 * Convert Instant to LocalDate in user's timezone
	 *
	 * @param instant UTC instant
	 * @param timezone User's timezone
	 * @return LocalDate in user's timezone
	 */
	public static LocalDate toLocalDate(Instant instant, ZoneId timezone) {
		return instant.atZone(timezone).toLocalDate();
	}
}
