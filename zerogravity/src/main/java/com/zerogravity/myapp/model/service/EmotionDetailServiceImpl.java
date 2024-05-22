package com.zerogravity.myapp.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zerogravity.myapp.model.dao.EmotionReasonDao;
import com.zerogravity.myapp.model.dto.EmotionReason;

@Service
public class EmotionDetailServiceImpl implements EmotionDetailService {
	
	private final EmotionReasonDao emotionReasonDao;
	
	@Autowired
	public EmotionDetailServiceImpl(EmotionReasonDao emotionDetailDao) {
		this.emotionReasonDao = emotionDetailDao;
	}

	@Override
	public List<EmotionReason> getEmotionReason() {
		return emotionReasonDao.selectEmotionReason();
	}
	

}
