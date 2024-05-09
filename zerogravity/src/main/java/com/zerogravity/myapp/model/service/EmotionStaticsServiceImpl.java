package com.zerogravity.myapp.model.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.zerogravity.myapp.model.dao.EmotionStaticsDao;

public class EmotionStaticsServiceImpl implements EmotionStaticsService {
	
	private final EmotionStaticsDao emotionStaticsDao;
	
	@Autowired
	public EmotionStaticsServiceImpl(EmotionStaticsDao emotionStaticsDao) {
		this.emotionStaticsDao = emotionStaticsDao;
	}

	@Override
	public double getEmotionStaticsWeekly(long userId) {
		return emotionStaticsDao.selectAvgWeekly(userId);
	}                                          

	@Override
	public double getEmotionStaticsMonthly(long userId) {
		return emotionStaticsDao.selectAvgMonthly(userId);
	}

	@Override
	public double getEmotionStaticsYearly(long userId) {
		return emotionStaticsDao.selectAvgYearly(userId);
	}

}
