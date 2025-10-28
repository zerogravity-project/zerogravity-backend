package com.zerogravity.myapp.controller;

import com.zerogravity.myapp.model.dto.*;
import com.zerogravity.myapp.model.service.ChartService;
import com.zerogravity.myapp.security.AuthUserId;
import com.zerogravity.myapp.util.TimezoneUtil;
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
@RequestMapping("/api-zerogravity/chart")
@Tag(name = "Chart Statistics", description = "차트 통계 API")
public class ChartRestController {

	private final ChartService chartService;

	@Autowired
	public ChartRestController(ChartService chartService) {
		this.chartService = chartService;
	}

	@GetMapping("/level")
	@Operation(summary = "감정 레벨 통계 조회", description = "기간별(주/월/년) 감정 레벨 평균 통계를 조회합니다.")
	@ApiResponses(value = {
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "통계 조회 성공"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청")
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
	@Operation(summary = "감정 이유 통계 조회", description = "기간별(주/월/년) 감정 이유 통계를 조회합니다.")
	@ApiResponses(value = {
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "통계 조회 성공"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청")
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
	@Operation(summary = "감정 개수 통계 조회", description = "기간별(주/월/년) 감정 개수 통계를 조회합니다 (Scatter Chart용).")
	@ApiResponses(value = {
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "통계 조회 성공"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청")
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
