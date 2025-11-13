package com.zerogravity.myapp.ai.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import com.zerogravity.myapp.ai.dto.SummaryData;
import com.zerogravity.myapp.ai.exception.GeminiApiException;
import com.zerogravity.myapp.chart.dto.ChartCountResponse;
import com.zerogravity.myapp.chart.dto.ChartLevelResponse;
import com.zerogravity.myapp.chart.dto.ChartReasonResponse;
import com.zerogravity.myapp.common.util.TimezoneUtil;
import com.zerogravity.myapp.emotion.dao.EmotionDao;
import com.zerogravity.myapp.emotion.dto.Emotion;
import com.zerogravity.myapp.emotion.dto.EmotionRecord;
import org.springframework.stereotype.Service;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of GeminiService
 * Integrates with Google Gemini API to generate emotion analysis summaries
 */
@Service
public class GeminiServiceImpl implements GeminiService {

	private final Client geminiClient;
	private final EmotionDao emotionDao;
	private final ObjectMapper objectMapper;
	private static final String MODEL_NAME = "gemini-2.5-flash";

	public GeminiServiceImpl(Client geminiClient, EmotionDao emotionDao) {
		this.geminiClient = geminiClient;
		this.emotionDao = emotionDao;
		this.objectMapper = new ObjectMapper();
	}

	@Override
	public SummaryData generateSummary(String period,
									   String startDateStr,
									   String endDateStr,
									   ChartLevelResponse levelChart,
									   ChartReasonResponse reasonChart,
									   ChartCountResponse countChart,
									   List<EmotionRecord> emotionRecords,
									   ZoneId timezone) {
		try {
			// Build prompt with emotion data
			String prompt = buildPrompt(period, startDateStr, endDateStr, levelChart, reasonChart, countChart, emotionRecords, timezone);

			// Call Gemini API
			GenerateContentResponse response = geminiClient.models.generateContent(
				MODEL_NAME,
				prompt,
				null
			);

			String responseText = response.text();

			// Parse response JSON
			SummaryData summaryData = parseResponse(responseText);

			return summaryData;
		} catch (Exception e) {
			throw new GeminiApiException("Failed to generate summary from Gemini API: " + e.getMessage(), e);
		}
	}

	/**
	 * Build prompt for Gemini API
	 * Combines emotion data in a structured format for AI analysis
	 */
	private String buildPrompt(String period,
							   String startDateStr,
							   String endDateStr,
							   ChartLevelResponse levelChart,
							   ChartReasonResponse reasonChart,
							   ChartCountResponse countChart,
							   List<EmotionRecord> emotionRecords,
							   ZoneId timezone) {

		StringBuilder prompt = new StringBuilder();
		prompt.append("You are an emotion analysis assistant. Analyze the following emotion data and provide insights.\n\n");

		prompt.append("Period: ").append(period).append(" (").append(startDateStr).append(" to ").append(endDateStr).append(")\n\n");

		// Level chart data
		if (levelChart != null && levelChart.getData() != null) {
			prompt.append("Emotion Level Statistics:\n");
			levelChart.getData().forEach(dataPoint -> {
				if (dataPoint.getValue() != null) {
					prompt.append("- ").append(dataPoint.getLabel()).append(": ").append(dataPoint.getValue()).append("\n");
				}
			});
			if (levelChart.getAverage() != null) {
				prompt.append("- Average Level: ").append(levelChart.getAverage()).append("\n");
			}
			prompt.append("\n");
		}

		// Reason chart data
		if (reasonChart != null && reasonChart.getData() != null) {
			prompt.append("Emotion Reasons Distribution:\n");
			reasonChart.getData().forEach(dataPoint ->
				prompt.append("- ").append(dataPoint.getLabel()).append(": ").append(dataPoint.getCount()).append(" times\n")
			);
			prompt.append("\n");
		}

		// Representative emotion records
		if (emotionRecords != null && !emotionRecords.isEmpty()) {
			// Load all emotions once for efficient lookup
			List<Emotion> allEmotions = emotionDao.selectAllEmotions();
			java.util.Map<Integer, String> emotionTypeMap = allEmotions.stream()
				.collect(Collectors.toMap(Emotion::getEmotionId, Emotion::getEmotionType));

			prompt.append("Representative Emotion Records:\n");
			emotionRecords.forEach(record -> {
				// Format timestamp in user timezone as ISO 8601
				java.time.Instant instant = record.getCreatedTime().toInstant();
				String timestamp = TimezoneUtil.formatToUserTimezone(instant, timezone);

				// Get emotion type from map
				String emotionType = emotionTypeMap.getOrDefault(record.getEmotionId(), "Unknown");

				// Format: [timestamp] emotionType (Level X): reasons - "diary..."
				prompt.append("- [").append(timestamp).append("] ")
					.append(emotionType).append(" (Level ").append(record.getEmotionId()).append("): ");

				if (record.getEmotionReasons() != null && !record.getEmotionReasons().isEmpty()) {
					prompt.append(String.join(", ", record.getEmotionReasons()));
				}

				if (record.getDiaryEntry() != null && !record.getDiaryEntry().isEmpty()) {
					String diaryExcerpt = record.getDiaryEntry().substring(0, Math.min(300, record.getDiaryEntry().length()));
					prompt.append(" - \"").append(diaryExcerpt);
					if (record.getDiaryEntry().length() > 300) {
						prompt.append("...");
					}
					prompt.append("\"");
				}

				prompt.append("\n");
			});
			prompt.append("\n");
		}

		prompt.append("Based on this data, provide a JSON response with the following structure:\n");
		prompt.append("{\n");
		prompt.append("  \"overview\": \"A 2-3 sentence summary of the emotion trends\",\n");
		prompt.append("  \"keyInsights\": [\"insight 1\", \"insight 2\", \"insight 3\"],\n");
		prompt.append("  \"recommendations\": [\"recommendation 1\", \"recommendation 2\"]\n");
		prompt.append("}\n\n");
		prompt.append("Respond ONLY with the JSON, no additional text.");

		return prompt.toString();
	}

	/**
	 * Parse Gemini API response as JSON
	 * Extracts overview, keyInsights, and recommendations
	 */
	private SummaryData parseResponse(String responseText) {
		try {
			// Extract JSON from response (Gemini might include extra text)
			String jsonStr = extractJson(responseText);

			// Parse JSON
			SummaryData summaryData = objectMapper.readValue(jsonStr, SummaryData.class);

			return summaryData;
		} catch (Exception e) {
			throw new GeminiApiException("Failed to parse Gemini API response: " + e.getMessage(), e);
		}
	}

	/**
	 * Extract JSON object from response text
	 * Handles cases where Gemini includes extra text before/after JSON
	 */
	private String extractJson(String text) {
		int startIdx = text.indexOf('{');
		int endIdx = text.lastIndexOf('}');

		if (startIdx == -1 || endIdx == -1 || startIdx >= endIdx) {
			throw new GeminiApiException("No valid JSON found in Gemini API response");
		}

		return text.substring(startIdx, endIdx + 1);
	}

	@Override
	public EmotionPredictionResult predictEmotion(String diaryEntry,
												  Integer providedEmotionId,
												  List<String> providedReasons) {
		try {
			// Build prediction prompt
			String prompt = buildPredictionPrompt(diaryEntry, providedEmotionId, providedReasons);

			// Call Gemini API
			GenerateContentResponse response = geminiClient.models.generateContent(
				MODEL_NAME,
				prompt,
				null
			);

			String responseText = response.text();

			// Parse response and create result (with null for provided fields)
			EmotionPredictionResult result = parsePredictionResponse(responseText, providedEmotionId, providedReasons);

			return result;
		} catch (Exception e) {
			throw new GeminiApiException("Failed to predict emotion from Gemini API: " + e.getMessage(), e);
		}
	}

	/**
	 * Build prompt for emotion prediction
	 * Requests prediction of missing information only
	 */
	private String buildPredictionPrompt(String diaryEntry, Integer providedEmotionId, List<String> providedReasons) {
		StringBuilder prompt = new StringBuilder();
		prompt.append("You are an emotion prediction assistant. Analyze the diary entry and predict missing emotion information.\n\n");

		prompt.append("Diary Entry:\n\"").append(diaryEntry).append("\"\n\n");

		// Predefined reasons list
		String[] predefinedReasons = {"Health", "Fitness", "Self-care", "Hobby", "Identity", "Religion", "Community",
			"Family", "Friends", "Partner", "Romance", "Money", "Housework", "Work", "Education", "Travel", "Weather",
			"Domestic Issues", "Global Issues"};

		prompt.append("Predefined emotion reasons (prioritize these):\n");
		for (String reason : predefinedReasons) {
			prompt.append("- ").append(reason).append("\n");
		}
		prompt.append("\n");

		// Instructions based on what's provided
		if (providedEmotionId != null) {
			prompt.append("The emotion level is already ").append(providedEmotionId).append(".\n");
			prompt.append("Predict ONLY the emotion reasons based on the diary entry.\n");
		} else if (providedReasons != null && !providedReasons.isEmpty()) {
			prompt.append("The emotion reasons are already: ").append(String.join(", ", providedReasons)).append(".\n");
			prompt.append("Predict ONLY the emotion level (0-6) based on the diary entry.\n");
		} else {
			prompt.append("Predict BOTH the emotion level (0-6) and emotion reasons based on the diary entry.\n");
		}

		prompt.append("\nIf none of the predefined reasons fit, you may suggest custom reasons.\n\n");

		prompt.append("Respond with ONLY a JSON object (no additional text):\n");
		if (providedEmotionId != null) {
			prompt.append("{\n");
			prompt.append("  \"emotionId\": null,\n");
			prompt.append("  \"reasons\": [\"reason1\", \"reason2\"],\n");
			prompt.append("  \"reasoning\": \"Explanation of why these reasons fit...\",\n");
			prompt.append("  \"confidence\": 0.85\n");
			prompt.append("}\n");
		} else if (providedReasons != null && !providedReasons.isEmpty()) {
			prompt.append("{\n");
			prompt.append("  \"emotionId\": 5,\n");
			prompt.append("  \"reasons\": null,\n");
			prompt.append("  \"reasoning\": \"Explanation of the predicted emotion level...\",\n");
			prompt.append("  \"confidence\": 0.85\n");
			prompt.append("}\n");
		} else {
			prompt.append("{\n");
			prompt.append("  \"emotionId\": 5,\n");
			prompt.append("  \"reasons\": [\"reason1\", \"reason2\"],\n");
			prompt.append("  \"reasoning\": \"Explanation of the prediction...\",\n");
			prompt.append("  \"confidence\": 0.85\n");
			prompt.append("}\n");
		}

		return prompt.toString();
	}

	/**
	 * Parse prediction response and create result with null for provided fields
	 */
	private EmotionPredictionResult parsePredictionResponse(String responseText, Integer providedEmotionId,
														   List<String> providedReasons) {
		try {
			String jsonStr = extractJson(responseText);
			com.fasterxml.jackson.databind.JsonNode root = objectMapper.readTree(jsonStr);

			Integer emotionId = root.has("emotionId") && !root.get("emotionId").isNull() ?
				root.get("emotionId").asInt() : null;
			List<String> reasons = null;
			if (root.has("reasons") && !root.get("reasons").isNull()) {
				reasons = objectMapper.convertValue(root.get("reasons"), new com.fasterxml.jackson.core.type.TypeReference<List<String>>(){});
			}
			String reasoning = root.has("reasoning") ? root.get("reasoning").asText() : "";
			Double confidence = root.has("confidence") ? root.get("confidence").asDouble() : 0.0;

			// Return null for provided fields
			return new EmotionPredictionResult(
				providedEmotionId != null ? null : emotionId,
				providedReasons != null ? null : reasons,
				reasoning,
				confidence
			);
		} catch (Exception e) {
			throw new GeminiApiException("Failed to parse prediction response: " + e.getMessage(), e);
		}
	}

	/**
	 * Summarize diary entries using Gemini API
	 * Generates a concise summary of multiple diary entries
	 */
	@Override
	public String summarizeDiaries(List<String> diaryEntries, int maxLength) {
		try {
			// Build prompt for diary summarization
			String prompt = buildDiarySummaryPrompt(diaryEntries, maxLength);

			// Call Gemini API
			GenerateContentResponse response = geminiClient.models.generateContent(
				MODEL_NAME,
				prompt,
				null
			);

			String responseText = response.text().trim();

			// Trim to max length if needed
			if (responseText.length() > maxLength) {
				responseText = responseText.substring(0, maxLength).trim();
			}

			return responseText;
		} catch (Exception e) {
			throw new GeminiApiException("Failed to summarize diaries from Gemini API: " + e.getMessage(), e);
		}
	}

	/**
	 * Build prompt for diary summarization
	 */
	private String buildDiarySummaryPrompt(List<String> diaryEntries, int maxLength) {
		StringBuilder prompt = new StringBuilder();
		prompt.append("You are a personal diary analyst. Summarize the following diary entries in a concise manner.\n\n");
		prompt.append("Diary Entries:\n");
		prompt.append("---\n");

		for (int i = 0; i < diaryEntries.size(); i++) {
			prompt.append(i + 1).append(". ").append(diaryEntries.get(i)).append("\n\n");
		}

		prompt.append("---\n\n");
		prompt.append("Please provide a summary of these entries that:\n");
		prompt.append("1. Captures the main themes and emotional patterns\n");
		prompt.append("2. Highlights key events and feelings\n");
		prompt.append("3. Is written in a natural, conversational tone\n");
		prompt.append("4. Is maximum ").append(maxLength).append(" characters long\n");
		prompt.append("5. Is in the same language as the diary entries\n\n");
		prompt.append("Respond with ONLY the summary text, no additional commentary.");

		return prompt.toString();
	}
}
