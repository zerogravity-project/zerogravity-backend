package com.zerogravity.myapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
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
    @Transactional
    public ResponseEntity<?> createAndManageRecords(@RequestBody EmotionRecord emotionRecord) {
        int created = emotionRecordService.createEmotionRecord(emotionRecord);

        if (created == 0) {
            throw new RuntimeException("Failed to create emotion record");
        } else {
        	boolean dailyStaticsUpdated = dailyStaticsService.updateOrCreateDailyStatics(emotionRecord);
        	if (!dailyStaticsUpdated) {
        		throw new RuntimeException("Failed to update daily statics");
        	}
//        	boolean periodicStaticsUpdated = periodicStaticsService.updateOrCreatePeriodicStatics(emotionRecord);
//        	if (!periodicStaticsUpdated) {
//        		throw new RuntimeException("Failed to update periodic statics");
//        	}
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


}
