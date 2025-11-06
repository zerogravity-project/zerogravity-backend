package com.zerogravity.myapp.common.exception;

import com.zerogravity.myapp.common.dto.ErrorResponse;
import com.zerogravity.myapp.common.util.TimezoneUtil;
import com.zerogravity.myapp.emotion.exception.DailyRecordAlreadyExistsException;
import com.zerogravity.myapp.emotion.exception.EditWindowExpiredException;
import com.zerogravity.myapp.emotion.exception.InvalidReasonException;
import com.zerogravity.myapp.emotion.exception.MomentNotEditableException;
import com.zerogravity.myapp.ai.exception.GeminiApiException;
import com.zerogravity.myapp.ai.exception.AIAnalysisCacheException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;
import java.time.ZoneId;

/**
 * Global exception handler for all REST controllers
 * Returns standardized error responses with timezone-aware timestamps
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(DailyRecordAlreadyExistsException.class)
	public ResponseEntity<ErrorResponse> handleDailyRecordAlreadyExists(
		DailyRecordAlreadyExistsException ex, WebRequest request) {

		String timezone = request.getHeader("X-Timezone");
		ZoneId zoneId = timezone != null ? ZoneId.of(timezone) : ZoneId.of("UTC");

		ErrorResponse error = new ErrorResponse(
			"DAILY_ALREADY_EXISTS",
			ex.getMessage(),
			TimezoneUtil.formatToUserTimezone(Instant.now(), zoneId)
		);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}

	@ExceptionHandler(MomentNotEditableException.class)
	public ResponseEntity<ErrorResponse> handleMomentNotEditable(
		MomentNotEditableException ex, WebRequest request) {

		String timezone = request.getHeader("X-Timezone");
		ZoneId zoneId = timezone != null ? ZoneId.of(timezone) : ZoneId.of("UTC");

		ErrorResponse error = new ErrorResponse(
			"MOMENT_NOT_EDITABLE",
			ex.getMessage(),
			TimezoneUtil.formatToUserTimezone(Instant.now(), zoneId)
		);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}

	@ExceptionHandler(EditWindowExpiredException.class)
	public ResponseEntity<ErrorResponse> handleEditWindowExpired(
		EditWindowExpiredException ex, WebRequest request) {

		String timezone = request.getHeader("X-Timezone");
		ZoneId zoneId = timezone != null ? ZoneId.of(timezone) : ZoneId.of("UTC");

		ErrorResponse error = new ErrorResponse(
			"EDIT_WINDOW_EXPIRED",
			ex.getMessage(),
			TimezoneUtil.formatToUserTimezone(Instant.now(), zoneId)
		);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}

	@ExceptionHandler(InvalidReasonException.class)
	public ResponseEntity<ErrorResponse> handleInvalidReason(
		InvalidReasonException ex, WebRequest request) {

		String timezone = request.getHeader("X-Timezone");
		ZoneId zoneId = timezone != null ? ZoneId.of(timezone) : ZoneId.of("UTC");

		ErrorResponse error = new ErrorResponse(
			"INVALID_REASON",
			ex.getMessage(),
			TimezoneUtil.formatToUserTimezone(Instant.now(), zoneId)
		);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}

	@ExceptionHandler(GeminiApiException.class)
	public ResponseEntity<ErrorResponse> handleGeminiApiException(
		GeminiApiException ex, WebRequest request) {

		String timezone = request.getHeader("X-Timezone");
		ZoneId zoneId = timezone != null ? ZoneId.of(timezone) : ZoneId.of("UTC");

		ErrorResponse error = new ErrorResponse(
			"GEMINI_API_ERROR",
			"Failed to generate AI analysis: " + ex.getMessage(),
			TimezoneUtil.formatToUserTimezone(Instant.now(), zoneId)
		);

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
	}

	@ExceptionHandler(AIAnalysisCacheException.class)
	public ResponseEntity<ErrorResponse> handleAIAnalysisCacheException(
		AIAnalysisCacheException ex, WebRequest request) {

		String timezone = request.getHeader("X-Timezone");
		ZoneId zoneId = timezone != null ? ZoneId.of(timezone) : ZoneId.of("UTC");

		ErrorResponse error = new ErrorResponse(
			"AI_ANALYSIS_CACHE_ERROR",
			"Failed to retrieve or cache analysis: " + ex.getMessage(),
			TimezoneUtil.formatToUserTimezone(Instant.now(), zoneId)
		);

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponse> handleIllegalArgument(
		IllegalArgumentException ex, WebRequest request) {

		String timezone = request.getHeader("X-Timezone");
		ZoneId zoneId = timezone != null ? ZoneId.of(timezone) : ZoneId.of("UTC");

		ErrorResponse error = new ErrorResponse(
			"INVALID_ARGUMENT",
			ex.getMessage(),
			TimezoneUtil.formatToUserTimezone(Instant.now(), zoneId)
		);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGenericException(
		Exception ex, WebRequest request) {

		String timezone = request.getHeader("X-Timezone");
		ZoneId zoneId = timezone != null ? ZoneId.of(timezone) : ZoneId.of("UTC");

		ErrorResponse error = new ErrorResponse(
			"INTERNAL_SERVER_ERROR",
			"An unexpected error occurred: " + ex.getMessage(),
			TimezoneUtil.formatToUserTimezone(Instant.now(), zoneId)
		);

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
	}
}
