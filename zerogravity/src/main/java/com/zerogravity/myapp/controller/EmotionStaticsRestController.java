package com.zerogravity.myapp.controller;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;

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

import com.zerogravity.myapp.model.dto.EmotionStatics;
import com.zerogravity.myapp.model.service.EmotionStaticsService;

@RestController
@RequestMapping("/api-zerogravity/statics")
public class EmotionStaticsRestController {
	
	private final EmotionStaticsService emotionStaticsService;
	
	@Autowired
	public EmotionStaticsRestController(EmotionStaticsService emotionStaticsService) {
		this.emotionStaticsService = emotionStaticsService;
	}
	
	// GET Emotion Statics: Weekly, Monthly, Yearly
	@GetMapping("/weekly/{userId}")
	public ResponseEntity<?> getEmotionStaticsWeekly(@PathVariable long userId) {
		LocalDate now = LocalDate.now();
		LocalDate startOfWeek = now.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
		LocalDate endOfWeek = now.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.SUNDAY));
		Timestamp startDate = Timestamp.valueOf(LocalDateTime.of(startOfWeek, LocalTime.MIN));
		Timestamp endDate = Timestamp.valueOf(LocalDateTime.of(endOfWeek, LocalTime.MAX));
		
		double weekly = emotionStaticsService.getEmotionStaticsWeekly(userId, startDate, endDate);
		
		if (weekly != 0) {
			return new ResponseEntity<Double>(weekly, HttpStatus.OK);
		} else {
			return new ResponseEntity<Double>(weekly, HttpStatus.NOT_FOUND);
		}
		
		
	}
	
	@GetMapping("/montly/{userId}")
	public ResponseEntity<Double> getEmotionStaticsMonthly(@PathVariable long userId) {
		LocalDate now = LocalDate.now();
		LocalDate startOfMonth = now.with(TemporalAdjusters.firstDayOfMonth());
		LocalDate endOfMonth = now.with(TemporalAdjusters.lastDayOfMonth());
		Timestamp startDate = Timestamp.valueOf(LocalDateTime.of(startOfMonth, LocalTime.MIN));
		Timestamp endDate = Timestamp.valueOf(LocalDateTime.of(endOfMonth, LocalTime.MAX));
		
		double monthly = emotionStaticsService.getEmotionStaticsMonthly(userId, startDate, endDate);
		
		if (monthly != 0) {
			return new ResponseEntity<Double>(monthly, HttpStatus.OK);
		} else {
			return new ResponseEntity<Double>(monthly, HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/yearly/{userId}")
	public ResponseEntity<Double> getEmotionStaticsYearly(@PathVariable long userId) {
		LocalDate now = LocalDate.now();
		LocalDate startOfYear = now.with(TemporalAdjusters.firstDayOfYear());
		LocalDate endOfYear = now.with(TemporalAdjusters.lastDayOfYear());
		Timestamp startDate = Timestamp.valueOf(LocalDateTime.of(startOfYear, LocalTime.MIN));
		Timestamp endDate = Timestamp.valueOf(LocalDateTime.of(endOfYear, LocalTime.MAX));
		
		double yearly = emotionStaticsService.getEmotionStaticsYearly(userId, startDate, endDate);
		
		if (yearly != 0) {
			return new ResponseEntity<Double>(yearly, HttpStatus.OK);
		} else {
			return new ResponseEntity<Double>(yearly, HttpStatus.NOT_FOUND);
		}
	}
	
	// POST Emotion Statics
	@PostMapping("/weekly")
	public ResponseEntity<?> createEmotionStaticsWeekly(@RequestBody EmotionStatics emotionStatics) {
		try {
			emotionStaticsService.createEmotionStaticsWeekly(emotionStatics);
			return new ResponseEntity<>(HttpStatus.CREATED);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping("/monthly")
	public ResponseEntity<?> createEmotionStaticsMonthly(@RequestBody EmotionStatics emotionStatics) {
		try {
			emotionStaticsService.createEmotionStaticsMonthly(emotionStatics);
			return new ResponseEntity<>(HttpStatus.CREATED);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping("/yearly")
	public ResponseEntity<?> createEmotionStaticsYearly(@RequestBody EmotionStatics emotionStatics) {
		try {
			emotionStaticsService.createEmotionStaticsYearly(emotionStatics);
			return new ResponseEntity<>(HttpStatus.CREATED);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	// PUT Emotion Statics: Weekly, Monthly, Yearly
	@PutMapping("/weekly/{userId}")
	public ResponseEntity<?> modifyEmotionStaticsWeekly(@PathVariable long userId, @RequestBody EmotionStatics emotionStatics) {
		try {
			boolean updated = emotionStaticsService.modifyEmotionStaticsWeekly(userId, emotionStatics);
			if (updated) {
				return new ResponseEntity<>(HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/montly/{userId}")
	public ResponseEntity<?> modifyEmotionStaticsMonthly(@PathVariable long userId, @RequestBody EmotionStatics emotionStatics) {
		try {
			boolean updated = emotionStaticsService.modifyEmotionStaticsMonthly(userId, emotionStatics);
			if (updated) {
				return new ResponseEntity<>(HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/yearly/{userId}")
	public ResponseEntity<?> modifyEmotionStaticsYearly(@PathVariable long userId, @RequestBody EmotionStatics emotionStatics) {
		try {
			boolean updated = emotionStaticsService.modifyEmotionStaticsYearly(userId, emotionStatics);
			if (updated) {
				return new ResponseEntity<>(HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
