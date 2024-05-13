package com.zerogravity.myapp.model.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zerogravity.myapp.model.dao.EmotionRecordDao;
import com.zerogravity.myapp.model.dto.EmotionRecord;

@Service
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
	@Transactional
	public int createEmotionRecord(EmotionRecord record) {
		return emotionRecordDao.createEmotionRecord(record);
	}

	@Override
	@Transactional
	public boolean updateEmotionRecord(EmotionRecord record) {
		return emotionRecordDao.updateEmotionRecord(record);
	}

}
