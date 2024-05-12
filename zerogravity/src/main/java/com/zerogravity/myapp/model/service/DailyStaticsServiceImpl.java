package com.zerogravity.myapp.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zerogravity.myapp.model.dao.DailyStaticsDao;
import com.zerogravity.myapp.model.dto.DailyStatics;

@Service
public class DailyStaticsServiceImpl implements DailyStaticsService {
	
	private final DailyStaticsDao dailyStaticsDao;
	
	@Autowired
	public DailyStaticsServiceImpl(DailyStaticsDao dailyStaticsDao) {
		this.dailyStaticsDao = dailyStaticsDao;
	}

	@Override
	public DailyStatics getDailyStaticsByUserId(long userId) {
		return dailyStaticsDao.selectDailyStatics(userId);
	}

//	@Override
//	@Transactional
//	public boolean createDailyStatics(DailyStatics dailyStatics) {
//		int result = dailyStaticsDao.insertDailyStatics(dailyStatics);
//		return result == 1;
//	}

	@Override
	@Transactional
	public boolean modifyDailyStatics(DailyStatics dailyStatics) {
	    DailyStatics existingStats = dailyStaticsDao.selectDailyStatics(dailyStatics.getDailyStaticsId());

	    if (existingStats != null) {
	        int result = dailyStaticsDao.updateDailyStatics(dailyStatics);
	        return result == 1;
	    } else {
	        int result = dailyStaticsDao.insertDailyStatics(dailyStatics);
	        return result == 1;
	    }
	}


}
