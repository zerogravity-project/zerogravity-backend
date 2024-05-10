package com.zerogravity.myapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zerogravity.myapp.model.dto.EmotionDetail;
import com.zerogravity.myapp.model.service.EmotionDetailService;

@RestController
@RequestMapping()
public class EmotionDetailRestController {
	private final EmotionDetailService emotionDetailService;
	
	@Autowired
	public EmotionDetailRestController(EmotionDetailService emotionDetailService) {
		this.emotionDetailService = emotionDetailService;
	}
	
	// GET Emotion Detail
	@GetMapping()
	public ResponseEntity<?> getEmotionDetail(String emotionId) {
		
		List<EmotionDetail> emotionList = emotionDetailService.getEmotionDetail(emotionId);
		
		if (emotionList == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<List<EmotionDetail>>(emotionList, HttpStatus.OK);
	}

}
