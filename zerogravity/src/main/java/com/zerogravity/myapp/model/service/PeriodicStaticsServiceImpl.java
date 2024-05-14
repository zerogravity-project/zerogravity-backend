package com.zerogravity.myapp.model.service;

import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zerogravity.myapp.model.dao.PeriodicStaticsDao;
import com.zerogravity.myapp.model.dto.EmotionRecord;
import com.zerogravity.myapp.model.dto.PeriodicStatics;

@Service
public class PeriodicStaticsServiceImpl implements PeriodicStaticsService {

    private final PeriodicStaticsDao periodicStaticsDao;

    @Autowired
    public PeriodicStaticsServiceImpl(PeriodicStaticsDao periodicStaticsDao) {
        this.periodicStaticsDao = periodicStaticsDao;
    }

    @Override
    public PeriodicStatics getPeriodicStaticsByUserId(long userId) {
        return periodicStaticsDao.selectPeriodicStatics(userId);
    }

    @Override
    @Transactional
    public boolean createOrModifyPeriodicStatics(EmotionRecord emotionRecord, Timestamp createdTime) {
    	
        Timestamp recordDateTime = createdTime;
        
        long userId = emotionRecord.getUserId();
        int scoreToAdd = emotionRecord.getEmotionRecordType(); 

        boolean weeklySuccess = processPeriodicStatics(userId, recordDateTime, "weekly", scoreToAdd);
        boolean monthlySuccess = processPeriodicStatics(userId, recordDateTime, "monthly", scoreToAdd);
        boolean yearlySuccess = processPeriodicStatics(userId, recordDateTime, "yearly", scoreToAdd);

        return weeklySuccess && monthlySuccess && yearlySuccess;
    }
    
    // 사용자 ID, 기록 생성 시점, 기록 타입, 넣어줄 감정 레벨에 따른 *기간 시작 시점 및 종료 시점 반환 메서드 
    // *기록 시점을 기준으로 해당 주/월/연도의 시작 기간 및 종료 기간 
    private boolean processPeriodicStatics(long userId, Timestamp recordDateTime, String periodType, int scoreToAdd) {
    	
        LocalDateTime dateTime = recordDateTime.toLocalDateTime();
        Timestamp periodStart = null;
        Timestamp periodEnd = null;
        
        switch (periodType) {
            case "weekly":
                periodStart = Timestamp.valueOf(dateTime.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).toLocalDate().atStartOfDay());
                periodEnd = Timestamp.valueOf(dateTime.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).toLocalDate().atStartOfDay());
                break;
            case "monthly":
                periodStart = Timestamp.valueOf(dateTime.with(TemporalAdjusters.firstDayOfMonth()).toLocalDate().atStartOfDay());
                periodEnd = Timestamp.valueOf(dateTime.with(TemporalAdjusters.lastDayOfMonth()).toLocalDate().atStartOfDay());
                break;
            case "yearly":
                periodStart = Timestamp.valueOf(dateTime.with(TemporalAdjusters.firstDayOfYear()).toLocalDate().atStartOfDay());
                periodEnd = Timestamp.valueOf(dateTime.with(TemporalAdjusters.lastDayOfYear()).toLocalDate().atStartOfDay());
                break;
        }
        
        // 기간 시작 시점, 종료 시점, 사용자 ID로 감정 통계 기록 조회 
        PeriodicStatics statics = periodicStaticsDao.selectPeriodicStaticsByPeriodAndUserId(userId, periodStart, periodEnd);
        
        // 기록 시점을 기준으로 weekly/monthly/yearly 데이터가 없으면 생성  
        if (statics == null) {
        	
            statics = new PeriodicStatics();
            // 새로운 고유 ID 부여 
            String newId = UUID.randomUUID().toString();
            
            statics.setPeriodicStaticsId(newId); 
            statics.setUserId(userId);
            statics.setPeriodStart(periodStart);
            statics.setPeriodEnd(periodEnd);
            statics.setPeriodType(periodType);
            statics.setPeriodicCount(1);
            statics.setPeriodicSum(scoreToAdd);
            statics.setPeriodicAverage(scoreToAdd);
            
            periodicStaticsDao.insertPeriodicStatics(statics);
        
        // 기록 시점을 기준으로 weekly/monthly/yearly 데이터가 있으면 업데이트 
        } else {
        	
            int newCount = statics.getPeriodicSum() + 1;
            int newSumScore = statics.getPeriodicSum() + scoreToAdd;
            double newAverageScore = (double) newSumScore / newCount;
            
            statics.setPeriodicStaticsId(statics.getPeriodicStaticsId());
            statics.setPeriodicCount(newCount);
            statics.setPeriodicSum(newSumScore);
            statics.setPeriodicAverage(newAverageScore);
            
            periodicStaticsDao.updatePeriodicStatics(statics);
        }
        return true;
    }
}