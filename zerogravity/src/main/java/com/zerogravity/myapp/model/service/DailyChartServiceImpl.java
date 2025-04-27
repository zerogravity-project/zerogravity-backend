package com.zerogravity.myapp.model.service;

import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zerogravity.myapp.model.dao.DailyChartDao;
import com.zerogravity.myapp.model.dto.DailyChart;
import com.zerogravity.myapp.model.dto.EmotionRecord;

@Service
public class DailyChartServiceImpl implements DailyChartService {
    
    private final DailyChartDao dailyChartDao;
    
    @Autowired
    public DailyChartServiceImpl(DailyChartDao dailyChartDao) {
        this.dailyChartDao = dailyChartDao;
    }
    
    @Override
    public List<DailyChart> getWeeklyChartByPeriodAndUserId(long userId, Timestamp searchDateTime) {
    	
    	List<DailyChart> weeklyChart = searchDailyChart(userId, searchDateTime, "weekly");
    	
    	return weeklyChart;
    }
    
    @Override
	public List<DailyChart> getMonthlyChartByPeriodAndUserId(long userId, Timestamp searchDateTime) {
	    
	    List<DailyChart> monthlyChart = searchDailyChart(userId, searchDateTime, "monthly");

	    return monthlyChart;
	}
    
    @Override
	public List<DailyChart> getYearlyChartByPeriodAndUserId(long userId, Timestamp searchDateTime) {
	    
	    List<DailyChart> yearlyChart = searchDailyChart(userId, searchDateTime, "yearly");

	    return yearlyChart;
	}
    
    private List<DailyChart> searchDailyChart(long userId, Timestamp searchDateTime, String periodType) {
        LocalDateTime dateTime = searchDateTime.toLocalDateTime();
        Timestamp periodStart = null;
        Timestamp periodEnd = null;

        switch (periodType) {
        case "weekly":
        	periodStart = Timestamp.valueOf(dateTime.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY)).toLocalDate().atStartOfDay());
        	periodEnd = Timestamp.valueOf(dateTime.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).toLocalDate().plusDays(1).atStartOfDay().minusNanos(1));
        	break;
            case "monthly":
                periodStart = Timestamp.valueOf(dateTime.with(TemporalAdjusters.firstDayOfMonth()).toLocalDate().atStartOfDay());
                periodEnd = Timestamp.valueOf(dateTime.with(TemporalAdjusters.lastDayOfMonth()).toLocalDate().plusDays(1).atStartOfDay().minusNanos(1));
                break;
            case "yearly":
                periodStart = Timestamp.valueOf(dateTime.with(TemporalAdjusters.firstDayOfYear()).toLocalDate().atStartOfDay());
                periodEnd = Timestamp.valueOf(dateTime.with(TemporalAdjusters.lastDayOfYear()).toLocalDate().plusDays(1).atStartOfDay().minusNanos(1));
                break;
        }

        return dailyChartDao.selectDailyChartByPeriodAndUserId(userId, periodStart, periodEnd);
    }


    @Override
    @Transactional
    public boolean createOrModifyDailyChart(EmotionRecord emotionRecord, Timestamp createdTime) {
    	
    	Timestamp recordTime = createdTime;

        long userId = emotionRecord.getUserId();
        int emotionLevel = emotionRecord.getEmotionRecordLevel();

        DailyChart existingChart = dailyChartDao.selectDailyChartByDateAndUserId(recordTime, userId);
        // 오늘 날짜의 기록이 없으면 새로 생성
        if(existingChart == null) {
        	
            // 새로운 고유 ID 부여 
            String newId = UUID.randomUUID().toString();

            DailyChart newChart = new DailyChart();
            newChart.setDailyChartId(newId);
            newChart.setUserId(userId);
			newChart.setDailySum(emotionLevel);
			newChart.setDailyCount(1);
			newChart.setDailyAverage(emotionLevel);
            newChart.setCreatedTime(recordTime);
            newChart.setUpdatedTime(recordTime);
            return dailyChartDao.insertDailyChart(newChart) == 1;
            
        // 오늘 날짜의 기록이 있으면 업데이트
        } else {
        	
        	int scoreToAdd = emotionRecord.getEmotionRecordLevel(); 
            int newCount = existingChart.getDailyCount() + 1;
            int newSumScore = existingChart.getDailySum() + scoreToAdd;
            double newAverageScore = (double) newSumScore / newCount;
            
            existingChart.setDailyChartId(existingChart.getDailyChartId());
            existingChart.setDailySum(newSumScore);
            existingChart.setDailyCount(newCount);
            existingChart.setDailyAverage(newAverageScore);
            existingChart.setUpdatedTime(Timestamp.from(Instant.now()));
            return dailyChartDao.updateDailyChart(existingChart) == 1;
        }
    }

}
