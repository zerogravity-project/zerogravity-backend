package com.zerogravity.myapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import com.zerogravity.myapp.model.dto.EmotionDetail;
import com.zerogravity.myapp.model.service.EmotionDetailService;

public class EmotionDetailRestController {
	private final EmotionDetailService emotionDetailService;
	
	@Autowired
	public EmotionDetailRestController(EmotionDetailService emotionDetailService) {
		this.emotionDetailService = emotionDetailService;
	}
	
	@GetMapping()
	public ResponseEntity<?> emotionDetail(String emotionId) {
		
		List<EmotionDetail> emotionList = emotionDetailService.getEmotionDetail(emotionId);
		
		if (emotionList == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<List<EmotionDetail>>(emotionList, HttpStatus.OK);
	}

}
