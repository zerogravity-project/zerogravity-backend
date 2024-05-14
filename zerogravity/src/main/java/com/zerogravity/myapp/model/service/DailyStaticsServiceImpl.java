package com.zerogravity.myapp.model.service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zerogravity.myapp.model.dao.DailyStaticsDao;
import com.zerogravity.myapp.model.dao.PeriodicStaticsDao;
import com.zerogravity.myapp.model.dto.DailyStatics;
import com.zerogravity.myapp.model.dto.EmotionRecord;

@Service
public class DailyStaticsServiceImpl implements DailyStaticsService {
    
    private final DailyStaticsDao dailyStaticsDao;
    
    @Autowired
    public DailyStaticsServiceImpl(DailyStaticsDao dailyStaticsDao) {
        this.dailyStaticsDao = dailyStaticsDao;
    }
    
    @Override
    @Transactional
    public boolean createOrModifyDailyStatics(EmotionRecord emotionRecord, Timestamp createdTime) {
    	
    	Timestamp recordTime = createdTime;

        long userId = emotionRecord.getUserId();
        int emotionType = emotionRecord.getEmotionRecordType();

        DailyStatics existingStatics = dailyStaticsDao.selectDailyStaticsByDateAndUserId(recordTime, userId);
        // 오늘 날짜의 기록이 없으면 새로 생성
        if (existingStatics == null) {
            DailyStatics newStatics = new DailyStatics();
            // 새로운 고유 ID 부여 
            String newId = UUID.randomUUID().toString();

            newStatics.setDailyStaticsId(newId);
            newStatics.setUserId(userId);
            newStatics.setDailySum(emotionType);
            newStatics.setCreatedTime(recordTime);
            newStatics.setUpdatedTime(recordTime);
            return dailyStaticsDao.insertDailyStatics(newStatics) == 1;
            
        // 오늘 날짜의 기록이 있으면 업데이트
        } else {
        	int scoreToAdd = emotionRecord.getEmotionRecordType(); 
            int newCount = existingStatics.getDailyCount() + 1;
            int newSumScore = existingStatics.getDailySum() + scoreToAdd;
            double newAverageScore = (double) newSumScore / newCount;
            
        	existingStatics.setDailyStaticsId(existingStatics.getDailyStaticsId());
            existingStatics.setDailySum(newSumScore);
            existingStatics.setDailyCount(newCount);
            existingStatics.setDailyAverage(newAverageScore);
            existingStatics.setUpdatedTime(Timestamp.from(Instant.now()));
            return dailyStaticsDao.updateDailyStatics(existingStatics) == 1;
        }
    }


}
