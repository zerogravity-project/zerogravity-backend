package com.zerogravity.myapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zerogravity.myapp.model.dto.EmotionStatics;
import com.zerogravity.myapp.model.service.EmotionStaticsService;

@RestController
@RequestMapping()
public class EmotionStaticsRestController {
	
	private final EmotionStaticsService emotionStaticsService;
	
	@Autowired
	public EmotionStaticsRestController(EmotionStaticsService emotionStaticsService) {
		this.emotionStaticsService = emotionStaticsService;
	}
	
	// GET Emotion Statics: Weekly, Monthly, Yearly
	@GetMapping()
	public ResponseEntity<Double> getEmotionStaticsWeekly(long userId) {
		double weekly = emotionStaticsService.getEmotionStaticsWeekly(userId);
		if (weekly != 0) {
			return new ResponseEntity<>(weekly, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping()
	public ResponseEntity<Double> getEmotionStaticsMonthly(long userId) {
		double monthly = emotionStaticsService.getEmotionStaticsMonthly(userId);
		if (monthly != 0) {
			return new ResponseEntity<>(monthly, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping()
	public ResponseEntity<Double> getEmotionStaticsYearly(long userId) {
		double yearly = emotionStaticsService.getEmotionStaticsYearly(userId);
		if (yearly != 0) {
			return new ResponseEntity<>(yearly, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	// POST Emotion Statics
	@PostMapping
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
	
	@PostMapping
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
	
	@PostMapping
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
	@PutMapping()
	public ResponseEntity<?> modifyEmotionStaticsWeekly(long userId, @RequestBody EmotionStatics emotionStatics) {
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
	
	@PutMapping()
	public ResponseEntity<?> modifyEmotionStaticsMonthly(long userId, @RequestBody EmotionStatics emotionStatics) {
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
	
	@PutMapping()
	public ResponseEntity<?> modifyEmotionStaticsYearly(long userId, @RequestBody EmotionStatics emotionStatics) {
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
