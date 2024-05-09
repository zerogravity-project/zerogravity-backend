package com.zerogravity.myapp.model.service;

import java.util.List;

import com.zerogravity.myapp.model.dto.EmotionDetail;

public interface EmotionDetailService {
	
	public abstract List<EmotionDetail> getEmotionDetail(String emotionId);

}
