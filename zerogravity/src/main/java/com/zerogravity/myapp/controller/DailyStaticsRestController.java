package com.zerogravity.myapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zerogravity.myapp.model.dto.DailyStatics;
import com.zerogravity.myapp.model.service.DailyStaticsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api-zerogravity/statics")
@Tag(name = "Daily Statics Management", description = "일일 감정 기록 API")
public class DailyStaticsRestController {

    private final DailyStaticsService dailyStaticsService;

    @Autowired
    public DailyStaticsRestController(DailyStaticsService dailyStaticsService) {
        this.dailyStaticsService = dailyStaticsService;
    }

    @GetMapping("/{userId}")
    @Operation(summary = "일일 감정 기록 조회", description = "특정 사용자의 일일 감정 기록을 조회합니다.")
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "200", description = "사용자 일일 감정 기록을 찾음"),
			@ApiResponse(responseCode = "404", description = "사용자 일일 감정 기록을 찾을 수 없음") 
	})
    public ResponseEntity<?> getDailyStaticsByUserId(@PathVariable long userId) {
        DailyStatics dailyStatics = dailyStaticsService.getDailyStaticsByUserId(userId);
        if (dailyStatics != null) {
            return new ResponseEntity<>(dailyStatics, HttpStatus.OK);
        } else {
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    @Operation(summary = "일일 감정 기록 생성", description = "특정 사용자의 일일 감정 기록을 생성합니다.")
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "200", description = "사용자 일일 감정 기록을 생성"),
			@ApiResponse(responseCode = "404", description = "사용자 일일 감정 기록을 생성할 수 없음") 
	})
    public ResponseEntity<Void> createDailyStatics(@Parameter(description = "사용자 일일 감정 기록") @RequestBody DailyStatics dailyStatics) {
        boolean result = dailyStaticsService.createDailyStatics(dailyStatics);
        if (result) {
        	return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
        	return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping
    @Operation(summary = "일일 감정 기록 업데이트", description = "특정 사용자의 일일 감정 기록을 업데이트합니다.")
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "200", description = "사용자 일일 감정 기록을 업데이트"),
			@ApiResponse(responseCode = "404", description = "사용자 일일 감정 기록을 업데이트할 수 없음") 
	})
    public ResponseEntity<Void> modifyDailyStatics(@Parameter(description = "사용자 일일 감정 기록") @RequestBody DailyStatics dailyStatics) {
        boolean result = dailyStaticsService.modifyDailyStatics(dailyStatics);
        if (result) {
        	return new ResponseEntity<Void>(HttpStatus.OK);
        } else {
        	return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
        }
    }
}
