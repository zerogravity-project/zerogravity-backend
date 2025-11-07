package com.zerogravity.myapp.ai.config;

import com.google.genai.Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for Gemini API client
 * Initializes and provides the Gemini API client as a Spring bean
 * Note: The API key should be set via environment variable GOOGLE_GENAI_API_KEY
 * or passed through the system property before client initialization
 */
@Configuration
public class GeminiConfig {

	@Value("${gemini.api.key}")
	private String apiKey;

	/**
	 * Create Gemini API client bean
	 * Initializes Client with API key from application-secure.properties
	 *
	 * @return Gemini API client
	 */
	@Bean
	public Client geminiClient() {
		// Create Client with API key directly
		return new Client.Builder()
			.apiKey(apiKey)
			.build();
	}
}
