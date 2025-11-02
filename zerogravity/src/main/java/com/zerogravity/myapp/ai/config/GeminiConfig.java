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
	 * Sets the API key from application-secure.properties to environment variable
	 * and returns a new Client instance
	 *
	 * @return Gemini API client
	 */
	@Bean
	public Client geminiClient() {
		// Set the API key from properties as environment variable for Client to use
		System.setProperty("GOOGLE_GENAI_API_KEY", apiKey);
		return new Client();
	}
}
