package com.zerogravity.myapp.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zerogravity.myapp.model.dao.EmotionDao;
import com.zerogravity.myapp.model.dto.Emotion;

@Service
@Transactional
public class EmotionServiceImpl implements EmotionService {
	
	private final EmotionDao emotionDao;
	
	@Autowired
	public EmotionServiceImpl(EmotionDao emotionDao) {
		this.emotionDao = emotionDao;
	}

	@Override
	public List<Emotion> getEmotionList() {
		return emotionDao.selectAllEmotions();
	}

}
