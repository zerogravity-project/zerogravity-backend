package com.zerogravity.myapp.ai.dao;

import com.zerogravity.myapp.ai.dto.EmotionAiAnalysis;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.sql.Timestamp;

/**
 * DAO interface for AI emotion analysis database operations
 * Handles storage and retrieval of AI-generated emotion predictions
 */
@Mapper
public interface EmotionAiAnalysisDao {

	/**
	 * Insert AI analysis record
	 *
	 * @param analysis Analysis record to insert
	 * @return Number of rows affected
	 */
	int insertAnalysis(EmotionAiAnalysis analysis);

	/**
	 * Insert a suggested reason for an analysis
	 *
	 * @param analysisId Analysis ID
	 * @param reason Emotion reason
	 * @return Number of rows affected
	 */
	int insertAnalysisReason(@Param("analysisId") Long analysisId, @Param("reason") String reason);

	/**
	 * Select analysis by ID with reasons loaded
	 *
	 * @param analysisId Analysis ID
	 * @return Analysis record with reasons, or null if not found
	 */
	EmotionAiAnalysis selectAnalysisById(@Param("analysisId") Long analysisId);

	/**
	 * Update analysis acceptance status
	 * Called when user confirms AI prediction and creates emotion record
	 *
	 * @param analysisId Analysis ID
	 * @param emotionRecordId Emotion record ID (created by user)
	 * @param acceptedAt Acceptance timestamp
	 * @return Number of rows affected
	 */
	int updateAccepted(@Param("analysisId") Long analysisId,
					  @Param("emotionRecordId") Long emotionRecordId,
					  @Param("acceptedAt") Timestamp acceptedAt);
}
