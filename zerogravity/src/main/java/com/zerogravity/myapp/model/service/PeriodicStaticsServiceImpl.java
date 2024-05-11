package com.zerogravity.myapp.model.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zerogravity.myapp.model.dao.PeriodicStaticsDao;
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
    public boolean upsertPeriodicStatics(PeriodicStatics periodicStatics) {
        if (!isValidInput(periodicStatics)) {
            return false;
        }

        PeriodicStatics existingStatics = periodicStaticsDao.selectPeriodicStaticsByUserAndType(
                periodicStatics.getUserId(), periodicStatics.getPeriodType());

        if (existingStatics == null || shouldStartNewRecord(existingStatics, periodicStatics.getPeriodType())) {
            return periodicStaticsDao.insertPeridodicStatics(periodicStatics) == 1;
        } else {
            return updateExistingStatics(existingStatics, periodicStatics);
        }
    }

    private boolean isValidInput(PeriodicStatics periodicStatics) {
        if (periodicStatics == null || periodicStatics.getPeriodType() == null) {
            return false;
        }
        return periodicStatics.getPeriodType().equals("weekly") ||
               periodicStatics.getPeriodType().equals("monthly") ||
               periodicStatics.getPeriodType().equals("yearly");
    }

    private boolean shouldStartNewRecord(PeriodicStatics existingStatics, String periodType) {
        LocalDate today = LocalDate.now();
        LocalDate lastPeriodEnd = parseDate(existingStatics.getPeriodEnd());

        switch (periodType) {
            case "weekly":
                return lastPeriodEnd.with(TemporalAdjusters.next(DayOfWeek.MONDAY)).isBefore(today);
            case "monthly":
                return lastPeriodEnd.with(TemporalAdjusters.firstDayOfNextMonth()).isBefore(today);
            case "yearly":
                return lastPeriodEnd.with(TemporalAdjusters.firstDayOfNextYear()).isBefore(today);
            default:
                return false;
        }
    }

    private LocalDate parseDate(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(dateString, formatter);
    }

    private boolean updateExistingStatics(PeriodicStatics existingStatics, PeriodicStatics newStatics) {
        int newCount = existingStatics.getCount() + 1;
        int newSumScore = existingStatics.getSumScore() + newStatics.getSumScore();
        double newAverage = (double) newSumScore / newCount;

        existingStatics.setSumScore(newSumScore);
        existingStatics.setCount(newCount);
        existingStatics.setAverageScore(newAverage);

        return periodicStaticsDao.updatePeriodiccStatics(existingStatics) == 1;
    }
}
