package com.zerogravity.myapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zerogravity.myapp.model.dto.PeriodicStatics;
import com.zerogravity.myapp.model.service.PeriodicStaticsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api-zerogravity/statics")
@Tag(name = "Periodic Statics Management", description = "감정 통계 기록 API")
public class PeriodicStaticsRestController {

    private final PeriodicStaticsService periodicStaticsService;

    @Autowired
    public PeriodicStaticsRestController(PeriodicStaticsService periodicStaticsService) {
        this.periodicStaticsService = periodicStaticsService;
    }

    @GetMapping("/{userId}")
    @Operation(summary = "감정 통계 기록 조회", description = "특정 사용자의 감정 통계 기록을 조회합니다.")
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "200", description = "사용자 감정 통계 기록을 찾음"),
			@ApiResponse(responseCode = "404", description = "사용자 감정 통계 기록을 찾을 수 없음") 
	})
    public ResponseEntity<?> getPeriodicStaticsByUserId(@PathVariable long userId) {
        PeriodicStatics periodicStatics = periodicStaticsService.getPeriodicStaticsByUserId(userId);
        if (periodicStatics != null) {
            return new ResponseEntity<PeriodicStatics>(periodicStatics, HttpStatus.OK);
        } else {
        	return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    @Operation(summary = "감정 통계 기록 생성 또는 업데이트", description = "특정 사용자의 감정 통계 기록 생성 또는 업데이트합니다.")
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "200", description = "사용자의 감정 통계 기록 생성 또는 업데이트"),
			@ApiResponse(responseCode = "404", description = "사용자의 감정 통계 기록 생성 또는 업데이트를 할 수 없음") 
	})
    public ResponseEntity<Void> upsertPeriodicStatics(@Parameter(description = "감정 통계 기록") @RequestBody PeriodicStatics periodicStatics) {
        boolean result = periodicStaticsService.upsertPeriodicStatics(periodicStatics);
        if (result) {
        	return new ResponseEntity<Void>(HttpStatus.CREATED);
        } else {
        	return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }
    }
}
