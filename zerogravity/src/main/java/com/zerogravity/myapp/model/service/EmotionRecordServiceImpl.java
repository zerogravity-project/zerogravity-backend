package com.zerogravity.myapp.model.service;

import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.IsoFields;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Locale;

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
		

	    List<EmotionRecord> monthlyChart = searchDailyChart(userId, searchDateTime, "monthly");
	    if (monthlyChart != null) {
	    	return monthlyChart;
	    }
	    return null;
	    
	}
	
	private List<EmotionRecord> searchDailyChart(long userId, Timestamp searchDateTime, String periodType) {
	    LocalDateTime dateTime = searchDateTime.toLocalDateTime();
	    Timestamp periodStart = null;
	    Timestamp periodEnd = null;
	    
	    switch (periodType) {
	        case "weekly":
	            periodStart = Timestamp.valueOf(dateTime.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY)).toLocalDate().atStartOfDay());
	            periodEnd = Timestamp.valueOf(dateTime.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY)).toLocalDate().atStartOfDay());
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
        
        return emotionRecordDao.selectEmotionRecordByPeriodAndUserIdForChart(userId, periodStart, periodEnd);
    }
	
	@Override
	@Transactional
	public int createEmotionRecord(EmotionRecord record) {
		int result = emotionRecordDao.createEmotionRecord(record);

		// Insert emotion reasons into junction table
		if (record.getEmotionReasons() != null && !record.getEmotionReasons().isEmpty()) {
			for (String reason : record.getEmotionReasons()) {
				emotionRecordDao.insertEmotionReason(record.getEmotionRecordId(), reason);
			}
		}

		return result;
	}

	@Override
	@Transactional
	public boolean updateEmotionRecord(EmotionRecord record) {
		boolean result = emotionRecordDao.updateEmotionRecord(record);

		// Delete existing emotion reasons and insert new ones
		if (result) {
			emotionRecordDao.deleteEmotionReasons(record.getEmotionRecordId());

			if (record.getEmotionReasons() != null && !record.getEmotionReasons().isEmpty()) {
				for (String reason : record.getEmotionReasons()) {
					emotionRecordDao.insertEmotionReason(record.getEmotionRecordId(), reason);
				}
			}
		}

		return result;
	}

	@Override
	public List<EmotionRecord> getEmotionRecordByYearAndMonth(long userId, int year, int month) {
		LocalDateTime startDateTime = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime endDateTime = startDateTime.with(TemporalAdjusters.lastDayOfMonth()).toLocalDate().atTime(23, 59, 59);

        Timestamp periodStart = Timestamp.valueOf(startDateTime);
        Timestamp periodEnd = Timestamp.valueOf(endDateTime);

        return emotionRecordDao.selectEmotionRecordByPeriodAndUserId(userId, periodStart, periodEnd);
	}

	@Override
	public List<EmotionRecord> getEmotionRecordByYearMonthWeek(long userId, int year, int month, int week) {
		// ISO 8601 주차 계산
		WeekFields weekFields = WeekFields.of(Locale.KOREA);

		// 해당 년도의 첫 날짜
		LocalDate firstDayOfYear = LocalDate.of(year, 1, 1);

		// 해당 주의 시작 날짜 (월요일) 계산
		LocalDate startOfWeek = firstDayOfYear
			.with(IsoFields.WEEK_OF_WEEK_BASED_YEAR, week)
			.with(weekFields.dayOfWeek(), 1); // 월요일

		LocalDate endOfWeek = startOfWeek.plusDays(6); // 일요일

		// 해당 월에 포함되지 않으면 범위 조정
		LocalDate monthStart = LocalDate.of(year, month, 1);
		LocalDate monthEnd = monthStart.with(TemporalAdjusters.lastDayOfMonth());

		if (startOfWeek.isAfter(monthEnd) || endOfWeek.isBefore(monthStart)) {
			return new ArrayList<>(); // 해당 주가 이 달에 없음
		}

		// 범위를 월로 제한
		if (startOfWeek.isBefore(monthStart)) {
			startOfWeek = monthStart;
		}
		if (endOfWeek.isAfter(monthEnd)) {
			endOfWeek = monthEnd;
		}

		Timestamp periodStart = Timestamp.valueOf(startOfWeek.atStartOfDay());
		Timestamp periodEnd = Timestamp.valueOf(endOfWeek.atTime(23, 59, 59));

		return emotionRecordDao.selectEmotionRecordByPeriodAndUserId(userId, periodStart, periodEnd);
	}

	@Override
	public List<EmotionRecord> getEmotionRecordByDate(long userId, LocalDate date) {
		Timestamp periodStart = Timestamp.valueOf(date.atStartOfDay());
		Timestamp periodEnd = Timestamp.valueOf(date.atTime(23, 59, 59));

		return emotionRecordDao.selectEmotionRecordByPeriodAndUserId(userId, periodStart, periodEnd);
	}

}
