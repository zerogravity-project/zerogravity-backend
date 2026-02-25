package com.zerogravity.myapp.emotion.controller;

import com.zerogravity.myapp.emotion.dto.*;
import com.zerogravity.myapp.emotion.service.EmotionRecordService;
import com.zerogravity.myapp.common.dto.ApiResponse;
import com.zerogravity.myapp.common.security.AuthUserId;
import com.zerogravity.myapp.common.util.TimezoneUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/emotions")
@Tag(name = "Emotion Record Management", description = "Emotion Record Management API")
public class EmotionRecordRestController {

	private final EmotionRecordService emotionRecordService;

	@Autowired
	public EmotionRecordRestController(EmotionRecordService emotionRecordService) {
		this.emotionRecordService = emotionRecordService;
	}

	@PostMapping("/records")
	@Operation(summary = "Create Emotion Record", description = "Create a new emotion record. Daily type can only be created once per day.")
	@ApiResponses(value = {
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Emotion record created successfully"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request (Daily duplicate, invalid reasons, etc.)")
	})
	public ResponseEntity<?> createEmotionRecord(
		@AuthUserId Long userId,
		@RequestHeader(value = "X-Timezone", defaultValue = "UTC") String clientTimezone,
		@Valid @RequestBody CreateEmotionRecordRequest request
	) {
		try {
			ZoneId timezone = ZoneId.of(clientTimezone);

			Long emotionRecordId = emotionRecordService.createEmotionRecord(
				userId,
				request.getEmotionId(),
				request.getEmotionRecordType(),
				request.getEmotionReasons(),
				request.getDiaryEntry(),
				timezone,
				request.getAiAnalysisId(),
				request.getRecordDate()
			);

			Map<String, Object> responseData = new HashMap<>();
			responseData.put("emotionRecordId", String.valueOf(emotionRecordId));

			ApiResponse<Map<String, Object>> response = new ApiResponse<>(
				responseData,
				TimezoneUtil.formatToUserTimezone(Instant.now(), timezone)
			);

			return ResponseEntity.status(HttpStatus.CREATED).body(response);
		} catch (Exception e) {
			// GlobalExceptionHandler will handle specific exceptions
			throw e;
		}
	}

	@GetMapping("/records")
	@Operation(summary = "Get Emotion Records", description = "Retrieve emotion records for a specific period, separated by Daily/Moment type.")
	@ApiResponses(value = {
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Retrieved successfully"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid parameters")
	})
	public ResponseEntity<?> getEmotionRecords(
		@AuthUserId Long userId,
		@RequestHeader(value = "X-Timezone", defaultValue = "UTC") String clientTimezone,
		@RequestParam String startDateTime,
		@RequestParam String endDateTime
	) {
		try {
			ZoneId timezone = ZoneId.of(clientTimezone);

			// Parse user input datetime strings to UTC Instant
			Instant periodStart = TimezoneUtil.parseToUtc(startDateTime, timezone);
			Instant periodEnd = TimezoneUtil.parseToUtc(endDateTime, timezone);

			// Get records
			List<EmotionRecord> records = emotionRecordService.getEmotionRecordByPeriodAndUserId(
				userId, periodStart, periodEnd
			);

			// Separate into daily and moment
			List<GetEmotionRecordsResponse.EmotionRecordDetail> daily = new ArrayList<>();
			List<GetEmotionRecordsResponse.EmotionRecordDetail> moment = new ArrayList<>();

			for (EmotionRecord record : records) {
				GetEmotionRecordsResponse.EmotionRecordDetail detail = new GetEmotionRecordsResponse.EmotionRecordDetail(
					String.valueOf(record.getEmotionRecordId()),
					record.getEmotionId(),
					getEmotionType(record.getEmotionId()),
					record.getEmotionReasons(),
					record.getDiaryEntry(),
					TimezoneUtil.formatToUserTimezone(record.getCreatedTime().toInstant(), timezone)
				);

				if (record.getEmotionRecordType() == EmotionRecord.Type.DAILY) {
					daily.add(detail);
				} else {
					moment.add(detail);
				}
			}

			GetEmotionRecordsResponse data = new GetEmotionRecordsResponse(daily, moment);
			ApiResponse<GetEmotionRecordsResponse> response = new ApiResponse<>(
				data,
				TimezoneUtil.formatToUserTimezone(Instant.now(), timezone)
			);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			throw new IllegalArgumentException("Invalid datetime format or timezone: " + e.getMessage());
		}
	}

	@PutMapping("/records/{emotionRecordId}")
	@Operation(summary = "Update Emotion Record", description = "Update an emotion record. Moment type cannot be modified, Daily can only be modified within 24 hours of creation.")
	@ApiResponses(value = {
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Updated successfully"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Cannot modify (Moment type, 24 hours exceeded)"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Record not found")
	})
	public ResponseEntity<?> updateEmotionRecord(
		@AuthUserId Long userId,
		@PathVariable Long emotionRecordId,
		@RequestHeader(value = "X-Timezone", defaultValue = "UTC") String clientTimezone,
		@Valid @RequestBody UpdateEmotionRecordRequest request
	) {
		try {
			ZoneId timezone = ZoneId.of(clientTimezone);

			boolean updated = emotionRecordService.updateEmotionRecord(
				userId,
				emotionRecordId,
				request.getEmotionId(),
				request.getEmotionReasons(),
				request.getDiaryEntry(),
				timezone
			);

			if (updated) {
				ApiResponse<Void> response = new ApiResponse<>(
					null,
					TimezoneUtil.formatToUserTimezone(Instant.now(), timezone)
				);
				return ResponseEntity.ok(response);
			} else {
				throw new IllegalArgumentException("Emotion record not found");
			}
		} catch (Exception e) {
			// GlobalExceptionHandler will handle specific exceptions
			throw e;
		}
	}

	// Helper method to get emotion type from ID
	private String getEmotionType(Integer emotionId) {
		switch (emotionId) {
			case 0: return "VERY_NEGATIVE";
			case 1: return "NEGATIVE";
			case 2: return "SLIGHTLY_NEGATIVE";
			case 3: return "NORMAL";
			case 4: return "SLIGHTLY_POSITIVE";
			case 5: return "POSITIVE";
			case 6: return "VERY_POSITIVE";
			default: return "UNKNOWN";
		}
	}
}
