package com.zerogravity.myapp.model.service;

import java.util.List;

import com.zerogravity.myapp.model.dto.EmotionReason;

public interface EmotionDetailService {
	
	public abstract List<EmotionReason> getEmotionReason();

}
