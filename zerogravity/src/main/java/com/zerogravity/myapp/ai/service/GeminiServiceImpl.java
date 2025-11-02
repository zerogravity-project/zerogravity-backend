package com.zerogravity.myapp.ai.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import com.zerogravity.myapp.ai.dto.SummaryData;
import com.zerogravity.myapp.ai.exception.GeminiApiException;
import com.zerogravity.myapp.chart.dto.ChartCountResponse;
import com.zerogravity.myapp.chart.dto.ChartLevelResponse;
import com.zerogravity.myapp.chart.dto.ChartReasonResponse;
import com.zerogravity.myapp.emotion.dto.EmotionRecord;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of GeminiService
 * Integrates with Google Gemini API to generate emotion analysis summaries
 */
@Service
public class GeminiServiceImpl implements GeminiService {

	private final Client geminiClient;
	private final ObjectMapper objectMapper;
	private static final String MODEL_NAME = "gemini-2.5-flash";

	public GeminiServiceImpl(Client geminiClient) {
		this.geminiClient = geminiClient;
		this.objectMapper = new ObjectMapper();
	}

	@Override
	public SummaryData generateSummary(String period,
									   String startDateStr,
									   String endDateStr,
									   ChartLevelResponse levelChart,
									   ChartReasonResponse reasonChart,
									   ChartCountResponse countChart,
									   List<EmotionRecord> emotionRecords) {
		try {
			// Build prompt with emotion data
			String prompt = buildPrompt(period, startDateStr, endDateStr, levelChart, reasonChart, countChart, emotionRecords);

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
							   List<EmotionRecord> emotionRecords) {

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

		// Individual records summary
		if (emotionRecords != null && !emotionRecords.isEmpty()) {
			prompt.append("Sample Diary Entries and Reasons:\n");
			emotionRecords.stream().limit(10).forEach(record -> {
				prompt.append("- Emotion Level ").append(record.getEmotionId()).append(": ");
				if (record.getEmotionReasons() != null && !record.getEmotionReasons().isEmpty()) {
					prompt.append(String.join(", ", record.getEmotionReasons()));
				}
				if (record.getDiaryEntry() != null && !record.getDiaryEntry().isEmpty()) {
					prompt.append(" - \"").append(record.getDiaryEntry().substring(0, Math.min(100, record.getDiaryEntry().length()))).append("...\"");
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
}
