package com.zerogravity.myapp.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zerogravity.myapp.model.dto.EmotionRecord;
import com.zerogravity.myapp.model.service.EmotionRecordService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api-zerogravity/emotions")
public class EmotionRecordRestController {

    @Autowired
    private EmotionRecordService emotionRecordService;
    
    // GET Emotion Record
    @GetMapping("/users/{userId}/emotions/records")
    public ResponseEntity<?> getEmotionRecordsByUserId(@PathVariable long userId) {
        List<EmotionRecord> records = emotionRecordService.getEmotionRecordsByUserId(userId);
        if (records != null && !records.isEmpty()) {
            return new ResponseEntity<List<EmotionRecord>>(records, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    // POST Emotion Record
    @PostMapping("/records")
    public ResponseEntity<?> createEmotionRecord(@RequestBody EmotionRecord record) {
        int created = emotionRecordService.createEmotionRecord(record);
        if (created == 0) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    // PUT Emotion Record
    @PutMapping("/records/{recordId}")
    public ResponseEntity<?> modifyEmotionRecord(@PathVariable String recordId, @Valid @RequestBody EmotionRecord record, BindingResult bindingResult) {
        
    	if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        boolean updated = emotionRecordService.updateEmotionRecord(record);
        if (updated) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
