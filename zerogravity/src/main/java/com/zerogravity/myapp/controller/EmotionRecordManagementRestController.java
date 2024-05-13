package com.zerogravity.myapp.controller;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zerogravity.myapp.model.dto.Emotion;
import com.zerogravity.myapp.model.dto.EmotionRecord;
import com.zerogravity.myapp.model.service.DailyStaticsService;
import com.zerogravity.myapp.model.service.EmotionRecordService;
import com.zerogravity.myapp.model.service.PeriodicStaticsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api-zerogravity/emotions")
@Tag(name = "Emotion Record Management", description = "감정 기록 및 통계 관리 API")
public class EmotionRecordManagementRestController {

    private final EmotionRecordService emotionRecordService;
    private final DailyStaticsService dailyStaticsService;
    private final PeriodicStaticsService periodicStaticsService;

    @Autowired
    public EmotionRecordManagementRestController(EmotionRecordService emotionRecordService, DailyStaticsService dailyStaticsService, PeriodicStaticsService periodicStaticsService) {
        this.emotionRecordService = emotionRecordService;
        this.dailyStaticsService = dailyStaticsService;
        this.periodicStaticsService = periodicStaticsService;
    }

    @PostMapping("/records")
    @Operation(summary = "감정 기록 생성 및 통계 생성 또는 업데이트", description = "감정 기록을 생성함과 동시에 통계 기록을 생성 또는 업데이트 합니다.")
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "201", description = "감정 기록 생성 및 통계 생성 또는 업데이트 성공"),
			@ApiResponse(responseCode = "400", description = "잘못된 요청으로 인한 감정 기록 생성 및 통계 생성 또는 업데이트 실패")
	})
    public ResponseEntity<?> createAndManageRecords(@RequestBody EmotionRecord emotionRecord) {
    	
    	// 1. Emotion Record 생성 
        int createDailyRecord = emotionRecordService.createEmotionRecord(emotionRecord);
        
        // 기록을 생성하는 시점에 대한 날짜 및 시간 정보 
        Timestamp createdTime = emotionRecordService.getCreatedTimeByEmotionRecordId(emotionRecord.getEmotionRecordId());
        
        if (createDailyRecord == 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
        	// 2. Daily Statics 생성 또는 업데이트 
        	boolean dailyStaticsUpdated = dailyStaticsService.createOrModifyDailyStatics(emotionRecord, createdTime);
        	if (!dailyStaticsUpdated) {
        		throw new RuntimeException("Failed to update or create daily statics");
        	}
	    	// 3. Periodic Statics 생성 또는 업데이트 
        	boolean periodicStaticsUpdated = periodicStaticsService.createOrModifyPeriodicStatics(emotionRecord, createdTime);
	    	if (!periodicStaticsUpdated) {
	    		throw new RuntimeException("Failed to update or create periodic statics");
	    	}
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


}
