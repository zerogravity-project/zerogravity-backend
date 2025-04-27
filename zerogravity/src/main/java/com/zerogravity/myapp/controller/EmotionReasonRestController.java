package com.zerogravity.myapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zerogravity.myapp.model.dto.EmotionReason;
import com.zerogravity.myapp.model.service.EmotionDetailService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api-zerogravity/emotions")
@Tag(name = "Emotion Reason Management", description = "감정 이유 정보 관리 API")
public class EmotionReasonRestController {
	private final EmotionDetailService emotionDetailService;
	
	@Autowired
	public EmotionReasonRestController(EmotionDetailService emotionDetailService) {
		this.emotionDetailService = emotionDetailService;
	}
	
	@GetMapping("/reasons")
	@Operation(summary = "감정 이유 정보 조회" , description = "감정 이유 정보를 조회합니다.")
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "200", description = "감정 이유 정보 조회 성공"),
			@ApiResponse(responseCode = "204", description = "잘못된 요청으로 인해 감정 이유 조회 실패"),
	})
	public ResponseEntity<?> getEmotionDetail() {
		
		List<EmotionReason> emotionList = emotionDetailService.getEmotionReason();
		
		if (emotionList == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<List<EmotionReason>>(emotionList, HttpStatus.OK);
	}

}
