package com.zerogravity.myapp.ai.service;

import com.zerogravity.myapp.ai.dao.EmotionAiAnalysisDao;
import com.zerogravity.myapp.ai.dto.EmotionAiAnalysis;
import com.zerogravity.myapp.ai.dto.EmotionPredictionRequest;
import com.zerogravity.myapp.ai.dto.EmotionPredictionResponse;
import com.zerogravity.myapp.ai.exception.GeminiApiException;
import com.zerogravity.myapp.common.security.SnowflakeIdService;
import com.zerogravity.myapp.common.util.TimezoneUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;

/**
 * Implementation of EmotionPredictionService
 * Handles AI-powered emotion prediction with database persistence
 */
@Service
public class EmotionPredictionServiceImpl implements EmotionPredictionService {

	private final GeminiService geminiService;
	private final EmotionAiAnalysisDao aiAnalysisDao;
	private final SnowflakeIdService snowflakeIdService;

	public EmotionPredictionServiceImpl(GeminiService geminiService,
									   EmotionAiAnalysisDao aiAnalysisDao,
									   SnowflakeIdService snowflakeIdService) {
		this.geminiService = geminiService;
		this.aiAnalysisDao = aiAnalysisDao;
		this.snowflakeIdService = snowflakeIdService;
	}

	@Override
	@Transactional
	public EmotionPredictionResponse predictEmotion(Long userId, EmotionPredictionRequest request) {
		try {
			// 1. Validate request
			validatePredictionRequest(request);

			// 2. Call Gemini to predict
			EmotionPredictionResult predictionResult = geminiService.predictEmotion(
				request.getDiaryEntry(),
				request.getEmotionId(),
				request.getEmotionReasons()
			);

			// 3. Create analysis record
			Instant now = Instant.now();
			long analysisId = snowflakeIdService.generateId();
			Timestamp nowTimestamp = Timestamp.from(now);

			EmotionAiAnalysis analysis = new EmotionAiAnalysis(
				analysisId,
				userId,
				request.getDiaryEntry(),
				predictionResult.getEmotionId(),
				predictionResult.getReasoning(),
				predictionResult.getConfidence(),
				nowTimestamp,
				false,
				null,
				null,
				null  // suggestedReasons will be loaded from junction table when needed
			);

			// 4. Store analysis
			aiAnalysisDao.insertAnalysis(analysis);

			// 5. Store suggested reasons
			if (predictionResult.getReasons() != null && !predictionResult.getReasons().isEmpty()) {
				for (String reason : predictionResult.getReasons()) {
					aiAnalysisDao.insertAnalysisReason(analysisId, reason);
				}
			}

			// 6. Build response
			return new EmotionPredictionResponse(
				String.valueOf(analysisId),
				predictionResult.getEmotionId(),
				predictionResult.getReasons(),
				predictionResult.getReasoning(),
				predictionResult.getConfidence(),
				TimezoneUtil.formatToUserTimezone(now, ZoneId.of("UTC"))  // Default to UTC
			);

		} catch (Exception e) {
			if (e instanceof GeminiApiException) {
				throw (GeminiApiException) e;
			}
			throw new GeminiApiException("Failed to predict emotion: " + e.getMessage(), e);
		}
	}

	@Override
	@Transactional
	public EmotionAiAnalysis acceptPrediction(Long userId, Long analysisId, Long emotionRecordId) {
		try {
			// 1. Get the analysis to verify ownership
			EmotionAiAnalysis analysis = aiAnalysisDao.selectAnalysisById(analysisId);
			if (analysis == null || !analysis.getUserId().equals(userId)) {
				throw new IllegalArgumentException("Analysis not found or access denied");
			}

			// 2. Mark as accepted
			Instant now = Instant.now();
			Timestamp acceptedAtTimestamp = Timestamp.from(now);
			aiAnalysisDao.updateAccepted(analysisId, emotionRecordId, acceptedAtTimestamp);

			// 3. Return updated analysis
			return aiAnalysisDao.selectAnalysisById(analysisId);

		} catch (Exception e) {
			if (e instanceof IllegalArgumentException) {
				throw (IllegalArgumentException) e;
			}
			throw new RuntimeException("Failed to accept prediction: " + e.getMessage(), e);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public EmotionAiAnalysis getAnalysis(Long userId, Long analysisId) {
		try {
			// Get the analysis
			EmotionAiAnalysis analysis = aiAnalysisDao.selectAnalysisById(analysisId);
			if (analysis == null || !analysis.getUserId().equals(userId)) {
				throw new IllegalArgumentException("Analysis not found or access denied");
			}
			return analysis;

		} catch (Exception e) {
			if (e instanceof IllegalArgumentException) {
				throw (IllegalArgumentException) e;
			}
			throw new RuntimeException("Failed to get analysis: " + e.getMessage(), e);
		}
	}

	/**
	 * Validate emotion prediction request
	 * Requires diary entry and at least one field missing
	 */
	private void validatePredictionRequest(EmotionPredictionRequest request) {
		// Diary entry is required
		if (request.getDiaryEntry() == null || request.getDiaryEntry().trim().isEmpty()) {
			throw new IllegalArgumentException("Diary entry is required for emotion prediction");
		}

		// Must have at least one field to predict (emotionId or emotionReasons missing)
		if (request.getEmotionId() != null &&
			request.getEmotionReasons() != null &&
			!request.getEmotionReasons().isEmpty()) {
			throw new IllegalArgumentException("No data to predict. Both emotionId and emotionReasons are already provided.");
		}
	}
}
