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
    public boolean updateOrCreatePeriodicStatics(EmotionRecord emotionRecord, Timestamp createdTime) {
        Timestamp recordDateTime = createdTime;
        LocalDateTime dateTime = createdTime.toLocalDateTime();
        
        long userId = emotionRecord.getUserId();
        int scoreToAdd = emotionRecord.getEmotionRecordType(); 

        boolean weeklySuccess = processPeriodicStatics(userId, recordDateTime, "weekly", scoreToAdd);
        boolean monthlySuccess = processPeriodicStatics(userId, recordDateTime, "monthly", scoreToAdd);
        boolean yearlySuccess = processPeriodicStatics(userId, recordDateTime, "yearly", scoreToAdd);

        return weeklySuccess && monthlySuccess && yearlySuccess;
    }

    private boolean processPeriodicStatics(long userId, Timestamp recordDateTime, String periodType, int scoreToAdd) {
        LocalDateTime dateTime = recordDateTime.toLocalDateTime();
        Timestamp periodStart = Timestamp.valueOf(dateTime.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)));
        Timestamp periodEnd = Timestamp.valueOf(dateTime.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)));

        switch (periodType) {
            case "monthly":
                periodStart = Timestamp.valueOf(dateTime.with(TemporalAdjusters.firstDayOfMonth()));
                periodEnd = Timestamp.valueOf(dateTime.with(TemporalAdjusters.lastDayOfMonth()));
                break;
            case "yearly":
                periodStart = Timestamp.valueOf(dateTime.with(TemporalAdjusters.firstDayOfYear()));
                periodEnd = Timestamp.valueOf(dateTime.with(TemporalAdjusters.lastDayOfYear()));
                break;
        }
        
        PeriodicStatics statics = periodicStaticsDao.findByPeriodAndUserId(userId, periodStart, periodEnd);
        if (statics == null) {
            statics = new PeriodicStatics();
            String newId = UUID.randomUUID().toString();
            statics.setPeriodicStaticsId(newId); 
            statics.setUserId(userId);
            statics.setPeriodStart(periodStart);
            statics.setPeriodEnd(periodEnd);
            statics.setPeriodType(periodType);
            statics.setCount(1);
            statics.setSumScore(scoreToAdd);
            statics.setAverageScore(scoreToAdd);
            periodicStaticsDao.insertPeriodicStatics(statics);
        } else {
            int newCount = statics.getCount() + 1;
            int newSumScore = statics.getSumScore() + scoreToAdd;
            double newAverageScore = (double) newSumScore / newCount;
            statics.setPeriodicStaticsId(statics.getPeriodicStaticsId());
            statics.setCount(newCount);
            statics.setSumScore(newSumScore);
            statics.setAverageScore(newAverageScore);
            periodicStaticsDao.updatePeriodicStatics(statics);
        }
        return true;
    }
}
