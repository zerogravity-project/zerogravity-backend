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

@RestController
@RequestMapping("/api-zerogravity/statics")
public class DailyStaticsRestController {

    private final DailyStaticsService dailyStaticsService;

    @Autowired
    public DailyStaticsRestController(DailyStaticsService dailyStaticsService) {
        this.dailyStaticsService = dailyStaticsService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<DailyStatics> getDailyStaticsByUserId(@PathVariable long userId) {
        DailyStatics dailyStatics = dailyStaticsService.getDailyStaticsByUserId(userId);
        if (dailyStatics != null) {
            return ResponseEntity.ok(dailyStatics);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<String> createDailyStatics(@RequestBody DailyStatics dailyStatics) {
        boolean result = dailyStaticsService.createDailyStatics(dailyStatics);
        if (result) {
        	return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
        	return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping
    public ResponseEntity<String> modifyDailyStatics(@RequestBody DailyStatics dailyStatics) {
        boolean result = dailyStaticsService.modifyDailyStatics(dailyStatics);
        if (result) {
        	return new ResponseEntity<>(HttpStatus.OK);
        } else {
        	return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
