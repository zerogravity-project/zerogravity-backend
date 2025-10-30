package com.zerogravity.myapp.emotion.dao;

import java.util.List;

import com.zerogravity.myapp.emotion.dto.Emotion;

public interface EmotionDao {
	
	public abstract List<Emotion> selectAllEmotions();
	
}
