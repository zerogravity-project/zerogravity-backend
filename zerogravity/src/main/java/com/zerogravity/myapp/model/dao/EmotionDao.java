package com.zerogravity.myapp.model.dao;

import java.util.List;

import com.zerogravity.myapp.model.dto.Emotion;

public interface EmotionDao {
	
	public abstract List<Emotion> selectAll();
	
}
