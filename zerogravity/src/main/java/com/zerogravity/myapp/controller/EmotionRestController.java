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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api-zerogravity/emotions")
@Tag(name = "Emotion Management", description = "감정 정보 관리 API")
public class EmotionRestController {
	
	private final EmotionService emotionService;
	
	@Autowired
	public EmotionRestController(EmotionService emotionService) {
		this.emotionService = emotionService;
	}
	
	@GetMapping
	@Operation(summary = "감정 정보 조회" , description = "감정 정보를 조회합니다.")
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "200", description = "감정 정보 조회 성공"),
			@ApiResponse(responseCode = "404", description = "감정 정보가 없음") 
	})
	public ResponseEntity<?> getEmotionList() {
		List<Emotion> emotionList = emotionService.getEmotionList();
		if (emotionList != null) {
			return new ResponseEntity<List<Emotion>>(emotionList, HttpStatus.OK);
		}
		return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
	}

}
