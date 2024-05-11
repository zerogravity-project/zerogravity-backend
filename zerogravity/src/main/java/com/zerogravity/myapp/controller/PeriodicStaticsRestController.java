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

@RestController
@RequestMapping("/api-zerogravity/statics")
public class PeriodicStaticsRestController {

    private final PeriodicStaticsService periodicStaticsService;

    @Autowired
    public PeriodicStaticsRestController(PeriodicStaticsService periodicStaticsService) {
        this.periodicStaticsService = periodicStaticsService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getPeriodicStaticsByUserId(@PathVariable long userId) {
        PeriodicStatics periodicStatics = periodicStaticsService.getPeriodicStaticsByUserId(userId);
        if (periodicStatics != null) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
        	return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<?> upsertPeriodicStatics(@RequestBody PeriodicStatics periodicStatics) {
        boolean result = periodicStaticsService.upsertPeriodicStatics(periodicStatics);
        if (result) {
        	return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
        	return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
