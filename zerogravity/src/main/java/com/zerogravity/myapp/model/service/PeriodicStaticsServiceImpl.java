package com.zerogravity.myapp.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.zerogravity.myapp.model.dao.PeriodicStaticsDao;
import com.zerogravity.myapp.model.dto.PeriodicStatics;

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
        
        PeriodicStatics existingStatics = periodicStaticsDao.selectPeriodicStaticsByUserAndType(periodicStatics.getUserId(), periodicStatics.getPeriodType());
        
        if (existingStatics == null) {
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

    private boolean updateExistingStatics(PeriodicStatics existingStatics, PeriodicStatics newStatics) {
        int newCount = existingStatics.getCount() + 1;
        int newSumScore = existingStatics.getSumScore() + newStatics.getSumScore();
        double newAverage = newSumScore / newCount;
        
        existingStatics.setSumScore(newSumScore);
        existingStatics.setCount(newCount);
        existingStatics.setAverageScore(newAverage);
        return periodicStaticsDao.updatePeriodiccStatics(existingStatics) == 1;
    }

}
