package com.zerogravity.myapp.controller;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zerogravity.myapp.model.dto.EmotionRecord;
import com.zerogravity.myapp.model.service.DailyStaticsService;
import com.zerogravity.myapp.model.service.EmotionRecordService;
import com.zerogravity.myapp.model.service.PeriodicStaticsService;

@RestController
@RequestMapping("/api-zerogravity/emotions")
public class EmotionManagementRestController {

    private final EmotionRecordService emotionRecordService;
    private final DailyStaticsService dailyStaticsService;
    private final PeriodicStaticsService periodicStaticsService;

    @Autowired
    public EmotionManagementRestController(EmotionRecordService emotionRecordService, DailyStaticsService dailyStaticsService, PeriodicStaticsService periodicStaticsService) {
        this.emotionRecordService = emotionRecordService;
        this.dailyStaticsService = dailyStaticsService;
        this.periodicStaticsService = periodicStaticsService;
    }

    @PostMapping("/records")
    public ResponseEntity<?> createAndManageRecords(@RequestBody EmotionRecord emotionRecord) {
    	// Emotion Record 생성 
        int created = emotionRecordService.createEmotionRecord(emotionRecord);
        
        // 기록을 생성하는 시점에 대한 날짜 및 시간 정보 
        Timestamp createdTime = emotionRecordService.getCreatedTimeByEmotionRecordId(emotionRecord.getEmotionRecordId());
        
        if (created == 0) {
            throw new RuntimeException("Failed to create emotion record");
        } else {
        	// Daily Statics 생성 
        	boolean dailyStaticsUpdated = dailyStaticsService.updateOrCreateDailyStatics(emotionRecord, createdTime);
        	if (!dailyStaticsUpdated) {
        		throw new RuntimeException("Failed to update daily statics");
        	}
	    	// Periodic Statics 생성 
        	boolean periodicStaticsUpdated = periodicStaticsService.updateOrCreatePeriodicStatics(emotionRecord, createdTime);
	    	if (!periodicStaticsUpdated) {
	    		throw new RuntimeException("Failed to update periodic statics");
	    	}
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


}
