package com.zerogravity.myapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zerogravity.myapp.model.dto.Emotion;
import com.zerogravity.myapp.model.service.EmotionService;

@RestController
@RequestMapping("/api-zerogravity/emtoions")
public class EmotionRestController {
	
	private final EmotionService emotionService;
	
	@Autowired
	public EmotionRestController(EmotionService emotionService) {
		this.emotionService = emotionService;
	}
	
	// GET Emotion 
	@GetMapping
	public ResponseEntity<?> getEmotionList() {
		List<Emotion> emotionList = emotionService.getEmotionList();
		if (emotionList != null) {
			return new ResponseEntity<List<Emotion>>(emotionList, HttpStatus.OK);
		}
		return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
	}

}
