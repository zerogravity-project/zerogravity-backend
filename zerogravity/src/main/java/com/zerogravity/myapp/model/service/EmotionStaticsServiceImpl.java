package com.zerogravity.myapp.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zerogravity.myapp.model.dao.EmotionStaticsDao;
import com.zerogravity.myapp.model.dto.EmotionStatics;

@Service
@Transactional
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

	@Override
	public int createEmotionStaticsWeekly(EmotionStatics emotionStatics) {
		return emotionStaticsDao.insertAvgWeekly(emotionStatics);
		
	}

	@Override
	public int createEmotionStaticsMonthly(EmotionStatics emotionStatics) {
		return emotionStaticsDao.insertAvgWeekly(emotionStatics);
	}

	@Override
	public int createEmotionStaticsYearly(EmotionStatics emotionStatics) {
		return emotionStaticsDao.insertAvgMonthly(emotionStatics);
	}
	
	@Override
	public boolean modifyEmotionStaticsWeekly(long userId, EmotionStatics emotionStatics) {
		return emotionStaticsDao.updateAvgWeekly(userId, emotionStatics);
	}

	@Override
	public boolean modifyEmotionStaticsMonthly(long userId, EmotionStatics emotionStatics) {
		return emotionStaticsDao.updateAvgMontyly(userId, emotionStatics);
	}

	@Override
	public boolean modifyEmotionStaticsYearly(long userId, EmotionStatics emotionStatics) {
		return emotionStaticsDao.updateAvgYearly(userId, emotionStatics);
	}

}
