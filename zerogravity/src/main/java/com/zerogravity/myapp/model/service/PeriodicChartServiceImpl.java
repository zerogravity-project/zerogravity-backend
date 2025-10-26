package com.zerogravity.myapp.model.service;

import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zerogravity.myapp.model.dao.PeriodicChartDao;
import com.zerogravity.myapp.model.dto.EmotionRecord;
import com.zerogravity.myapp.model.dto.PeriodicChart;

@Service
public class PeriodicChartServiceImpl implements PeriodicChartService {

    private final PeriodicChartDao periodicChartDao;

    @Autowired
    public PeriodicChartServiceImpl(PeriodicChartDao periodicChartDao) {
        this.periodicChartDao = periodicChartDao;
    }

    @Override
    public PeriodicChart getPeriodicChartByUserId(long userId) {
        return periodicChartDao.selectPeriodicChart(userId);
    }
    
    @Override
    @Transactional
    public boolean createOrModifyPeriodicChart(EmotionRecord emotionRecord, Timestamp createdTime) {

        Timestamp recordDateTime = createdTime;

        long userId = emotionRecord.getUserId();
        int scoreToAdd = emotionRecord.getEmotionId();

        boolean weeklySuccess = processPeriodicChart(userId, recordDateTime, "weekly", scoreToAdd);
        boolean monthlySuccess = processPeriodicChart(userId, recordDateTime, "monthly", scoreToAdd);
        boolean yearlySuccess = processPeriodicChart(userId, recordDateTime, "yearly", scoreToAdd);

        return weeklySuccess && monthlySuccess && yearlySuccess;
    }
    
    // 사용자 ID, 기록 생성 시점, 기록 타입, 넣어줄 감정 레벨에 따른 *기간 시작 시점 및 종료 시점 반환 메서드 
    // *기록 시점을 기준으로 해당 주/월/연도의 시작 기간 및 종료 기간 
    private boolean processPeriodicChart(long userId, Timestamp recordDateTime, String periodType, int scoreToAdd) {
    	
        LocalDateTime dateTime = recordDateTime.toLocalDateTime();
        Timestamp periodStart = null;
        Timestamp periodEnd = null;
        
        switch (periodType) {
            case "weekly":
                periodStart = Timestamp.valueOf(dateTime.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY)).toLocalDate().atStartOfDay());
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
        PeriodicChart existingChart = periodicChartDao.selectPeriodicChartByPeriodAndUserId(userId, periodStart, periodEnd);
        
        // 기록 시점을 기준으로 weekly/monthly/yearly 데이터가 없으면 생성  
        if (existingChart == null) {
        	
            PeriodicChart newChart = new PeriodicChart();
            // 새로운 고유 ID 부여 
            String newId = UUID.randomUUID().toString();
            
            newChart.setPeriodicChartId(newId); 
            newChart.setUserId(userId);
            newChart.setPeriodStart(periodStart);
            newChart.setPeriodEnd(periodEnd);
            newChart.setPeriodType(periodType);
            newChart.setPeriodicCount(1);
            newChart.setPeriodicSum(scoreToAdd);
            newChart.setPeriodicAverage(scoreToAdd);
            newChart.setCreatedTime(recordDateTime);
            newChart.setUpdatedTime(recordDateTime);
            
            periodicChartDao.insertPeriodicChart(newChart);
        
        // 기록 시점을 기준으로 weekly/monthly/yearly 데이터가 있으면 업데이트 
        } else {
        	
            int newCount = existingChart.getPeriodicCount() + 1;
            int newSumScore = existingChart.getPeriodicSum() + scoreToAdd;
            double newAverageScore = (double) newSumScore / newCount;
            
            existingChart.setPeriodicChartId(existingChart.getPeriodicChartId());
            existingChart.setPeriodicCount(newCount);
            existingChart.setPeriodicSum(newSumScore);
            existingChart.setPeriodicAverage(newAverageScore);
            
            periodicChartDao.updatePeriodicChart(existingChart);
        }
        return true;
    }

}