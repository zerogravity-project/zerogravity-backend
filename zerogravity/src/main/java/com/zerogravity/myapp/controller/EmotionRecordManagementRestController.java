package com.zerogravity.myapp.controller;

import java.sql.Timestamp;
import java.util.List;

import com.zerogravity.myapp.security.AuthUserId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.zerogravity.myapp.model.dto.EmotionRecord;
import com.zerogravity.myapp.model.service.DailyChartService;
import com.zerogravity.myapp.model.service.EmotionRecordService;
import com.zerogravity.myapp.model.service.PeriodicChartService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api-zerogravity/emotions")
@Tag(name = "Emotion Record Management", description = "감정 기록 및 통계 관리 API")
public class EmotionRecordManagementRestController {

    private final EmotionRecordService emotionRecordService;
    private final DailyChartService dailyChartService;
    private final PeriodicChartService periodicChartService;

    @Autowired
    public EmotionRecordManagementRestController(EmotionRecordService emotionRecordService,
                                                 DailyChartService dailyChartService,
                                                 PeriodicChartService periodicChartService) {
        this.emotionRecordService = emotionRecordService;
        this.dailyChartService = dailyChartService;
        this.periodicChartService = periodicChartService;
    }

    @GetMapping("/records")
    @Operation(summary = "한달 감정 기록 불러오기", description = "한달의 감정 기록을 시간 순으로 불러옵니다.")
    public ResponseEntity<?> getWeeklyRecord(
            @AuthUserId Long userId,
            @RequestParam int year,
            @RequestParam int month
    ) {
        List<EmotionRecord> monthlyRecord = emotionRecordService.getEmotionRecordByYearAndMonth(userId, year, month);
        return new ResponseEntity<>(monthlyRecord, HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/records")
    @Operation(summary = "감정 기록 생성 및 통계 생성 또는 업데이트", description = "감정 기록을 생성함과 동시에 통계 기록을 생성 또는 업데이트 합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "감정 기록 생성 및 통계 생성 또는 업데이트 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청으로 인한 감정 기록 생성 및 통계 생성 또는 업데이트 실패")
    })
    public ResponseEntity<?> createAndManageRecords(
            @AuthUserId Long userId,
            @RequestBody EmotionRecord emotionRecord
    ) {
        // userId 설정
        emotionRecord.setUserId(userId);

        int createDailyRecord = emotionRecordService.createEmotionRecord(emotionRecord);
        Timestamp createdTime = emotionRecordService.getCreatedTimeByEmotionRecordId(emotionRecord.getEmotionRecordId());

        if (createDailyRecord == 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            boolean dailyChartUpdated = dailyChartService.createOrModifyDailyChart(emotionRecord, createdTime);
            if (!dailyChartUpdated) {
                throw new RuntimeException("Failed to update or create daily chart");
            }

            boolean periodicChartUpdated = periodicChartService.createOrModifyPeriodicChart(emotionRecord, createdTime);
            if (!periodicChartUpdated) {
                throw new RuntimeException("Failed to update or create periodic chart");
            }
        }

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Transactional
    @PutMapping("/records/{emotionRecordId}")
    @Operation(summary = "감정 기록 업데이트", description = "감정 기록을 업데이트를 합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "감정 기록 업데이트 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청으로 인해 감정 기록 업데이트 실패"),
            @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    public ResponseEntity<?> modifyEmotionRecord(
            @AuthUserId Long userId,
            @PathVariable("emotionRecordId") String emotionRecordId,
            @RequestBody EmotionRecord emotionRecord
    ) {
        // userId 설정 (사용자의 감정 기록만 수정 가능)
        emotionRecord.setUserId(userId);

        if (emotionRecordService.updateEmotionRecord(emotionRecord)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
