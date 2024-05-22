package com.zerogravity.myapp.model.dao;

import java.util.List;

import com.zerogravity.myapp.model.dto.EmotionReason;

public interface EmotionReasonDao {
	
	public abstract List<EmotionReason> selectEmotionReason();
	
}
