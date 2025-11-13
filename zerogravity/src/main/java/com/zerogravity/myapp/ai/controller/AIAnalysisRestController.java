package com.zerogravity.myapp.ai.controller;

import com.zerogravity.myapp.ai.dto.AIAnalysisResponse;
import com.zerogravity.myapp.ai.dto.DiarySummaryResponse;
import com.zerogravity.myapp.ai.dto.EmotionPredictionRequest;
import com.zerogravity.myapp.ai.dto.EmotionPredictionResponse;
import com.zerogravity.myapp.ai.service.AIAnalysisService;
import com.zerogravity.myapp.ai.service.EmotionPredictionService;
import com.zerogravity.myapp.common.dto.ApiResponse;
import com.zerogravity.myapp.common.security.AuthUserId;
import com.zerogravity.myapp.common.util.TimezoneUtil;
import com.zerogravity.myapp.user.service.UserService;
import com.zerogravity.myapp.user.dto.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.time.ZoneId;

/**
 * REST Controller for AI emotion analysis endpoints
 * Provides AI-powered emotion analysis summaries using Gemini API
 */
@RestController
@RequestMapping("/ai")
@Tag(name = "AI Analysis", description = "AI-powered emotion analysis API")
public class AIAnalysisRestController {

	private final AIAnalysisService aiAnalysisService;
	private final EmotionPredictionService emotionPredictionService;
	private final UserService userService;

	public AIAnalysisRestController(AIAnalysisService aiAnalysisService,
								   EmotionPredictionService emotionPredictionService,
								   UserService userService) {
		this.aiAnalysisService = aiAnalysisService;
		this.emotionPredictionService = emotionPredictionService;
		this.userService = userService;
	}

	/**
	 * Get AI emotion analysis summary for a specified period
	 * Returns cached result if available, otherwise generates new analysis
	 *
	 * @param userId Authenticated user ID (from JWT)
	 * @param clientTimezone User's timezone
	 * @param period Analysis period (week/month/year)
	 * @param startDate Start date in ISO 8601 format (YYYY-MM-DD)
	 * @return AI analysis response with summary
	 */
	@GetMapping("/period-analyses")
	@Operation(
		summary = "Get AI emotion analysis summary",
		description = "Returns an AI-generated summary of emotion data for the specified period. " +
					"Results are cached for 24 hours to reduce API calls."
	)
	@ApiResponses(value = {
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Analysis generated/retrieved successfully"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request parameters"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - invalid JWT"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "AI service error or cache error")
	})
	public ResponseEntity<?> getAIAnalysis(
		@AuthUserId Long userId,
		@RequestHeader(value = "X-Timezone", defaultValue = "UTC")
		@Parameter(description = "User's timezone (e.g., Asia/Seoul, America/New_York)")
		String clientTimezone,
		@RequestParam(value = "period")
		@Parameter(description = "Analysis period", schema = @Schema(allowableValues = {"week", "month", "year"}))
		String period,
		@RequestParam(value = "startDate")
		@Parameter(description = "Start date in ISO 8601 format (YYYY-MM-DD)")
		String startDate
	) {
		try {
			ZoneId timezone = ZoneId.of(clientTimezone);

			// Check AI analysis consent
			User user = userService.getUserByUserId(userId);
			if (user == null || !user.getAiAnalysisConsent()) {
				throw new IllegalArgumentException("AI analysis consent required. Please enable AI analysis consent in your preferences.");
			}

			// Validate period
			if (!period.matches("(?i)^(week|month|year)$")) {
				throw new IllegalArgumentException("Invalid period. Must be 'week', 'month', or 'year'");
			}

			// Get analysis
			AIAnalysisResponse analysisResponse = aiAnalysisService.getAnalysis(userId, period, startDate, timezone);

			// Wrap in API response
			ApiResponse<AIAnalysisResponse> apiResponse = new ApiResponse<>(
				analysisResponse,
				TimezoneUtil.formatToUserTimezone(Instant.now(), timezone)
			);

			return ResponseEntity.ok(apiResponse);

		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Invalid request: " + e.getMessage());
		} catch (Exception e) {
			throw new RuntimeException("Failed to get AI analysis: " + e.getMessage());
		}
	}

	/**
	 * Predict emotion information from diary entry
	 * Returns AI suggestions without creating emotion record
	 * User can then accept and create record with /emotion-record endpoint
	 *
	 * @param userId Authenticated user ID (from JWT)
	 * @param request Prediction request with diary entry and optional emotion info
	 * @return Prediction response with analysis ID and suggestions
	 */
	@PostMapping("/emotion-predictions")
	@Operation(
		summary = "Predict emotion from diary entry",
		description = "AI predicts missing emotion information (emotionId and/or emotionReasons) based on diary entry. " +
					"Returns analysis ID and suggestions. User can then create emotion record with the aiAnalysisId."
	)
	@ApiResponses(value = {
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Prediction generated successfully"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request - missing diary entry or both fields provided"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - invalid JWT"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "AI service error")
	})
	public ResponseEntity<?> predictEmotion(
		@AuthUserId Long userId,
		@Valid @RequestBody EmotionPredictionRequest request
	) {
		try {
			// Check AI analysis consent
			User user = userService.getUserByUserId(userId);
			if (user == null || !user.getAiAnalysisConsent()) {
				throw new IllegalArgumentException("AI analysis consent required. Please enable AI analysis consent in your preferences.");
			}

			// Predict emotion
			EmotionPredictionResponse predictionResponse = emotionPredictionService.predictEmotion(userId, request);

			// Wrap in API response
			ApiResponse<EmotionPredictionResponse> apiResponse = new ApiResponse<>(
				predictionResponse,
				TimezoneUtil.formatToUserTimezone(Instant.now(), ZoneId.of("UTC"))
			);

			return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);

		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Invalid request: " + e.getMessage());
		} catch (Exception e) {
			throw new RuntimeException("Failed to predict emotion: " + e.getMessage());
		}
	}

	/**
	 * Get diary summary for a specified period
	 * Summarizes diary entries using AI (requires at least 3 entries)
	 *
	 * @param userId Authenticated user ID (from JWT)
	 * @param clientTimezone User's timezone
	 * @param startDate Start date in ISO 8601 format (YYYY-MM-DD)
	 * @param endDate End date in ISO 8601 format (YYYY-MM-DD)
	 * @return Diary summary response with AI-generated summary
	 */
	@GetMapping("/diary-summaries")
	@Operation(
		summary = "Get diary summary",
		description = "Returns an AI-generated summary of diary entries within the specified period. " +
					"Requires at least 3 diary entries to generate summary."
	)
	@ApiResponses(value = {
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Summary generated successfully"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid parameters or insufficient diary entries"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - invalid JWT or no AI consent"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "AI service error")
	})
	public ResponseEntity<?> getDiarySummary(
		@AuthUserId Long userId,
		@RequestHeader(value = "X-Timezone", defaultValue = "UTC")
		@Parameter(description = "User's timezone (e.g., Asia/Seoul, America/New_York)")
		String clientTimezone,
		@RequestParam(value = "startDate")
		@Parameter(description = "Start date in ISO 8601 format (YYYY-MM-DD)")
		String startDate,
		@RequestParam(value = "endDate")
		@Parameter(description = "End date in ISO 8601 format (YYYY-MM-DD)")
		String endDate
	) {
		try {
			ZoneId timezone = ZoneId.of(clientTimezone);

			// Check AI analysis consent
			User user = userService.getUserByUserId(userId);
			if (user == null || !user.getAiAnalysisConsent()) {
				throw new IllegalArgumentException("AI analysis consent required. Please enable AI analysis consent in your preferences.");
			}

			// Get diary summary
			DiarySummaryResponse summaryResponse = aiAnalysisService.getDiarySummary(userId, startDate, endDate, timezone);

			// Wrap in API response
			ApiResponse<DiarySummaryResponse> apiResponse = new ApiResponse<>(
				summaryResponse,
				TimezoneUtil.formatToUserTimezone(Instant.now(), timezone)
			);

			return ResponseEntity.ok(apiResponse);

		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Invalid request: " + e.getMessage());
		} catch (Exception e) {
			throw new RuntimeException("Failed to get diary summary: " + e.getMessage());
		}
	}
}
