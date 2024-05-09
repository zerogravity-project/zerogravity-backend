package com.zerogravity.myapp.model.service;

import java.util.List;

import com.zerogravity.myapp.model.dao.EmotionRecordDao;
import com.zerogravity.myapp.model.dto.EmotionRecord;

public class EmotionRecordServiceImpl implements EmotionRecordService {
	
    private EmotionRecordDao emotionRecordDao;

    public EmotionRecordServiceImpl(EmotionRecordDao emotionRecordDao) {
        this.emotionRecordDao = emotionRecordDao;
    }

	@Override
	public List<EmotionRecord> getEmotionRecordsByUserId(long userId) {
		return emotionRecordDao.selectEmotionRecordByEmotionId(userId);
	}

	@Override
	public int createEmotionRecord(EmotionRecord record) {
		return emotionRecordDao.createEmotionRecord(record);
	}

	@Override
	public boolean updateEmotionRecord(EmotionRecord record) {
		return emotionRecordDao.updateEmotionRecord(record);
	}

}
