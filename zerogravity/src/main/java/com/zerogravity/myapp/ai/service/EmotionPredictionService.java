package com.zerogravity.myapp.ai.service;

import com.zerogravity.myapp.ai.dto.EmotionAiAnalysis;
import com.zerogravity.myapp.ai.dto.EmotionPredictionRequest;
import com.zerogravity.myapp.ai.dto.EmotionPredictionResponse;

/**
 * Service interface for emotion prediction
 * Handles AI-powered prediction of missing emotion information
 */
public interface EmotionPredictionService {

	/**
	 * Predict emotion information from diary entry
	 * Creates an analysis record and returns suggestions without saving the emotion record
	 *
	 * @param userId User ID
	 * @param request Prediction request with diary entry and optional emotion info
	 * @return Analysis ID and prediction suggestions
	 */
	EmotionPredictionResponse predictEmotion(Long userId, EmotionPredictionRequest request);

	/**
	 * Accept a prediction and mark analysis as accepted
	 * Links the analysis to the created emotion record
	 *
	 * @param userId User ID
	 * @param analysisId Analysis ID from prediction
	 * @param emotionRecordId The emotion record ID that was created with this analysis
	 * @return Updated analysis record
	 */
	EmotionAiAnalysis acceptPrediction(Long userId, Long analysisId, Long emotionRecordId);

	/**
	 * Get a prediction by analysis ID
	 *
	 * @param userId User ID
	 * @param analysisId Analysis ID
	 * @return Analysis record with prediction details
	 */
	EmotionAiAnalysis getAnalysis(Long userId, Long analysisId);
}
