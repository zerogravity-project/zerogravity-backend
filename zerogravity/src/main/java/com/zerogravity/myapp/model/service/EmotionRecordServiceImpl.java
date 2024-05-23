package com.zerogravity.myapp.model.service;

import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zerogravity.myapp.model.dao.EmotionRecordDao;
import com.zerogravity.myapp.model.dto.DailyChart;
import com.zerogravity.myapp.model.dto.EmotionRecord;

@Service
public class EmotionRecordServiceImpl implements EmotionRecordService {
	
    private EmotionRecordDao emotionRecordDao;

    public EmotionRecordServiceImpl(EmotionRecordDao emotionRecordDao) {
        this.emotionRecordDao = emotionRecordDao;
    }

	@Override
	public List<EmotionRecord> getEmotionRecordsByUserId(long userId) {
		return emotionRecordDao.selectEmotionRecordByUserId(userId);
	}

	@Override
	public Timestamp getCreatedTimeByEmotionRecordId(String emotionRecordId) {
		return emotionRecordDao.selectCreatedTimeByEmotionRecordId(emotionRecordId);
	}
	
	@Override
	public List<EmotionRecord> getEmotionRecordByPeriodAndUserId(long userId, Timestamp searchDateTime) {
		
	    Map<String, List<EmotionRecord>> chartMap = new HashMap<>();


	    List<EmotionRecord> monthlyStatics = searchDailyStatics(userId, searchDateTime, "monthly");
	    if (monthlyStatics != null) {
	    	chartMap.put("monthly", monthlyStatics);
	    }
	    
	    return monthlyStatics;
	}
    private List<EmotionRecord> searchDailyStatics(long userId, Timestamp searchDateTime, String periodType) {
        LocalDateTime dateTime = searchDateTime.toLocalDateTime();
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
        
        return emotionRecordDao.selectEmotionRecordByPeriodAndUserId(userId, periodStart, periodEnd);
    }
    
   public List<EmotionRecord> getWeeklyEmotionRecordByPeriodAndUserId(long userId, Timestamp searchDateTime) {
    	
    	List<EmotionRecord> weeklyChart = searchDailyEmotionRecord(userId, searchDateTime, "weekly");
    	
    	return weeklyChart;
    }
    
    @Override
	public List<EmotionRecord> getMonthlyEmotionRecordByPeriodAndUserId(long userId, Timestamp searchDateTime) {
	    
	    List<EmotionRecord> monthlyChart = searchDailyEmotionRecord(userId, searchDateTime, "monthly");

	    return monthlyChart;
	}
    
    @Override
	public List<EmotionRecord> getYearlyEmotionRecordByPeriodAndUserId(long userId, Timestamp searchDateTime) {
	    
	    List<EmotionRecord> yearlyChart = searchDailyEmotionRecord(userId, searchDateTime, "yearly");

	    return yearlyChart;
	}
    
    private List<EmotionRecord> searchDailyEmotionRecord(long userId, Timestamp searchDateTime, String periodType) {
        LocalDateTime dateTime = searchDateTime.toLocalDateTime();
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
        
        return emotionRecordDao.selectEmotionRecordByPeriodAndUserIdForChart(userId, periodStart, periodEnd);
    }
	
	@Override
	@Transactional
	public int createEmotionRecord(EmotionRecord record) {
		return emotionRecordDao.createEmotionRecord(record);
	}

	@Override
	public boolean updateEmotionRecord(EmotionRecord record) {
		return emotionRecordDao.updateEmotionRecord(record);
	}

	@Override
	public List<EmotionRecord> getEmotionRecordByYearAndMonth(long userId, int year, int month) {
		LocalDateTime startDateTime = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime endDateTime = startDateTime.with(TemporalAdjusters.lastDayOfMonth()).toLocalDate().atTime(23, 59, 59);

        Timestamp periodStart = Timestamp.valueOf(startDateTime);
        Timestamp periodEnd = Timestamp.valueOf(endDateTime);

        return emotionRecordDao.selectEmotionRecordByPeriodAndUserId(userId, periodStart, periodEnd);
	}
	


}
