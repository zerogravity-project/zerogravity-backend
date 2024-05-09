package com.zerogravity.myapp.model.dao;

import java.util.List;

import com.zerogravity.myapp.model.dto.EmotionDetail;

public interface EmotionDetailDao {
	
	public abstract List<EmotionDetail> selectEmotionDetailByEmotionId(String emotionId);
	
}
