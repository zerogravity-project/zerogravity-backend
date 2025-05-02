package com.zerogravity.myapp.controller;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.zerogravity.myapp.security.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.zerogravity.myapp.model.dto.DailyChart;
import com.zerogravity.myapp.model.dto.EmotionRecord;
import com.zerogravity.myapp.model.service.DailyChartService;
import com.zerogravity.myapp.model.service.EmotionRecordService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api-zerogravity/chart")
@Tag(name = "Chart Record Management", description = "차트 관련 기록 관리 API")
public class ChartRestController {

    private final EmotionRecordService emotionRecordService;
    private final DailyChartService dailyChartService;
    private final JWTUtil jwtUtil;

    @Autowired
    public ChartRestController(EmotionRecordService emotionRecordService,
                               DailyChartService dailyChartService,
                               JWTUtil jwtUtil) {
        this.emotionRecordService = emotionRecordService;
        this.dailyChartService = dailyChartService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/level")
    @Operation(summary = "기간별 통계 데이터 조회", description = "기간별(주/월/년) 통계 데이터를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "해당 기간에 대한 통계 데이터를 찾음"),
            @ApiResponse(responseCode = "204", description = "해당 기간에 대한 통계 데이터가 없음"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    public ResponseEntity<?> getLevelRecords(
            @CookieValue(value = "token", required = false) String token,
            @RequestParam String period,
            @RequestParam Timestamp searchDate
    ) {
        Optional<Long> userIdOpt = jwtUtil.extractUserId(token);
        if (userIdOpt.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        long userId = userIdOpt.get();

        List<DailyChart> records;
        switch (period.toLowerCase()) {
            case "weekly":
                records = dailyChartService.getWeeklyChartByPeriodAndUserId(userId, searchDate);
                break;
            case "monthly":
                records = dailyChartService.getMonthlyChartByPeriodAndUserId(userId, searchDate);
                break;
            case "yearly":
                records = dailyChartService.getYearlyChartByPeriodAndUserId(userId, searchDate);
                break;
            default:
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (records != null && !records.isEmpty()) {
            return new ResponseEntity<>(records, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping("/count")
    @Operation(summary = "기간별 감정 데이터 조회", description = "기간별(주/월/년) 감정 데이터를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "해당 기간에 대한 감정 데이터를 찾음"),
            @ApiResponse(responseCode = "204", description = "해당 기간에 대한 감정 데이터가 없음"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    public ResponseEntity<?> getCountRecords(
            @CookieValue(value = "token", required = false) String token,
            @RequestParam String period,
            @RequestParam Timestamp searchDate
    ) {
        Optional<Long> userIdOpt = jwtUtil.extractUserId(token);
        if (userIdOpt.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        long userId = userIdOpt.get();

        List<EmotionRecord> records;
        switch (period.toLowerCase()) {
            case "weekly":
                records = emotionRecordService.getWeeklyEmotionRecordByPeriodAndUserId(userId, searchDate);
                break;
            case "monthly":
                records = emotionRecordService.getMonthlyEmotionRecordByPeriodAndUserId(userId, searchDate);
                break;
            case "yearly":
                records = emotionRecordService.getYearlyEmotionRecordByPeriodAndUserId(userId, searchDate);
                break;
            default:
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (records != null && !records.isEmpty()) {
            return new ResponseEntity<>(records, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
}
