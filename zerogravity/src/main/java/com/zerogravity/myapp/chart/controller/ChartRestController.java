package com.zerogravity.myapp.chart.controller;

import com.zerogravity.myapp.chart.dto.*;
import com.zerogravity.myapp.chart.service.ChartService;
import com.zerogravity.myapp.common.dto.ApiResponse;
import com.zerogravity.myapp.common.security.AuthUserId;
import com.zerogravity.myapp.common.util.TimezoneUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

@RestController
@RequestMapping("/chart")
@Tag(name = "Chart Statistics", description = "Chart Statistics API")
public class ChartRestController {

	private final ChartService chartService;

	@Autowired
	public ChartRestController(ChartService chartService) {
		this.chartService = chartService;
	}

	@GetMapping("/level")
	@Operation(summary = "Get Emotion Level Statistics", description = "Retrieve emotion level average statistics by period (weekly/monthly/yearly).")
	@ApiResponses(value = {
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Statistics retrieved successfully"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request")
	})
	public ResponseEntity<?> getEmotionLevelChart(
		@AuthUserId Long userId,
		@RequestHeader(value = "X-Timezone", defaultValue = "UTC") String clientTimezone,
		@RequestParam String period,
		@RequestParam String startDate
	) {
		try {
			ZoneId timezone = ZoneId.of(clientTimezone);
			LocalDate date = LocalDate.parse(startDate);

			ChartLevelResponse data = chartService.getEmotionLevelChart(userId, period, date, timezone);
			ApiResponse<ChartLevelResponse> response = new ApiResponse<>(
				data,
				TimezoneUtil.formatToUserTimezone(Instant.now(), timezone)
			);
			return ResponseEntity.ok(response);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Invalid period or date format: " + e.getMessage());
		}
	}

	@GetMapping("/reason")
	@Operation(summary = "Get Emotion Reason Statistics", description = "Retrieve emotion reason statistics by period (weekly/monthly/yearly).")
	@ApiResponses(value = {
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Statistics retrieved successfully"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request")
	})
	public ResponseEntity<?> getEmotionReasonChart(
		@AuthUserId Long userId,
		@RequestHeader(value = "X-Timezone", defaultValue = "UTC") String clientTimezone,
		@RequestParam String period,
		@RequestParam String startDate
	) {
		try {
			ZoneId timezone = ZoneId.of(clientTimezone);
			LocalDate date = LocalDate.parse(startDate);

			ChartReasonResponse data = chartService.getEmotionReasonChart(userId, period, date, timezone);
			ApiResponse<ChartReasonResponse> response = new ApiResponse<>(
				data,
				TimezoneUtil.formatToUserTimezone(Instant.now(), timezone)
			);
			return ResponseEntity.ok(response);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Invalid period or date format: " + e.getMessage());
		}
	}

	@GetMapping("/count")
	@Operation(summary = "Get Emotion Count Statistics", description = "Retrieve emotion count statistics by period (weekly/monthly/yearly) for Scatter Chart.")
	@ApiResponses(value = {
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Statistics retrieved successfully"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request")
	})
	public ResponseEntity<?> getEmotionCountChart(
		@AuthUserId Long userId,
		@RequestHeader(value = "X-Timezone", defaultValue = "UTC") String clientTimezone,
		@RequestParam String period,
		@RequestParam String startDate
	) {
		try {
			ZoneId timezone = ZoneId.of(clientTimezone);
			LocalDate date = LocalDate.parse(startDate);

			ChartCountResponse data = chartService.getEmotionCountChart(userId, period, date, timezone);
			ApiResponse<ChartCountResponse> response = new ApiResponse<>(
				data,
				TimezoneUtil.formatToUserTimezone(Instant.now(), timezone)
			);
			return ResponseEntity.ok(response);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Invalid period or date format: " + e.getMessage());
		}
	}
}
