package com.zerogravity.myapp.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zerogravity.myapp.model.dao.EmotionDetailDao;
import com.zerogravity.myapp.model.dto.EmotionDetail;

@Service
@Transactional
public class EmotionDetailServiceImpl implements EmotionDetailService {
	
	private final EmotionDetailDao emotionDetailDao;
	
	@Autowired
	public EmotionDetailServiceImpl(EmotionDetailDao emotionDetailDao) {
		this.emotionDetailDao = emotionDetailDao;
	}

	@Override
	public List<EmotionDetail> getEmotionDetail(String emotionId) {
		return emotionDetailDao.selectEmotionDetailByEmotionId(emotionId);
	}
	

}
