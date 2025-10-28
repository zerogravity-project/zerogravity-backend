package com.zerogravity.myapp.controller;

import com.zerogravity.myapp.model.dto.*;
import com.zerogravity.myapp.model.service.EmotionRecordService;
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
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api-zerogravity/emotions")
@Tag(name = "Emotion Record Management", description = "감정 기록 관리 API")
public class EmotionRecordRestController {

	private final EmotionRecordService emotionRecordService;

	@Autowired
	public EmotionRecordRestController(EmotionRecordService emotionRecordService) {
		this.emotionRecordService = emotionRecordService;
	}

	@PostMapping("/records")
	@Operation(summary = "감정 기록 생성", description = "새로운 감정 기록을 생성합니다. Daily 타입은 하루에 한 번만 생성 가능합니다.")
	@ApiResponses(value = {
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "감정 기록 생성 성공"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청 (Daily 중복, 잘못된 이유 등)")
	})
	public ResponseEntity<?> createEmotionRecord(
		@AuthUserId Long userId,
		@RequestHeader(value = "X-Timezone", defaultValue = "UTC") String clientTimezone,
		@RequestBody CreateEmotionRecordRequest request
	) {
		try {
			ZoneId timezone = ZoneId.of(clientTimezone);

			Long emotionRecordId = emotionRecordService.createEmotionRecord(
				userId,
				request.getEmotionId(),
				request.getEmotionRecordType(),
				request.getEmotionReasons(),
				request.getDiaryEntry(),
				timezone
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
	@Operation(summary = "감정 기록 조회", description = "특정 기간의 감정 기록을 Daily/Moment로 분리하여 조회합니다.")
	@ApiResponses(value = {
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 파라미터")
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
	@Operation(summary = "감정 기록 수정", description = "감정 기록을 수정합니다. Moment 타입은 수정 불가하며, Daily는 생성 후 24시간 이내에만 수정 가능합니다.")
	@ApiResponses(value = {
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "수정 성공"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "수정 불가 (Moment 타입, 24시간 초과)"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "기록을 찾을 수 없음")
	})
	public ResponseEntity<?> updateEmotionRecord(
		@AuthUserId Long userId,
		@PathVariable Long emotionRecordId,
		@RequestHeader(value = "X-Timezone", defaultValue = "UTC") String clientTimezone,
		@RequestBody UpdateEmotionRecordRequest request
	) {
		try {
			ZoneId timezone = ZoneId.of(clientTimezone);

			boolean updated = emotionRecordService.updateEmotionRecord(
				userId,
				emotionRecordId,
				request.getEmotionId(),
				request.getEmotionReasons(),
				request.getDiaryEntry()
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
			case 2: return "MID_NEGATIVE";
			case 3: return "NORMAL";
			case 4: return "MID_POSITIVE";
			case 5: return "POSITIVE";
			case 6: return "VERY_POSITIVE";
			default: return "UNKNOWN";
		}
	}
}
