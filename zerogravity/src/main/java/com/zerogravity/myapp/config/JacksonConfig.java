package com.zerogravity.myapp.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * Jackson Configuration for JSON Serialization
 *
 * Purpose: Serialize Long/BIGINT as String to prevent JavaScript precision loss
 *
 * JavaScript Number.MAX_SAFE_INTEGER = 2^53 - 1 (9007199254740991, ~16 digits)
 * Snowflake IDs are BIGINT (64-bit, up to 19 digits)
 *
 * Solution: Serialize all Long values as Strings in JSON responses
 * Example: userId: 123456789012345678 → "123456789012345678"
 */
@Configuration
public class JacksonConfig {

	@Bean
	@Primary
	public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
		ObjectMapper objectMapper = builder.createXmlMapper(false).build();

		// Register custom module to serialize Long as String
		SimpleModule module = new SimpleModule();
		module.addSerializer(Long.class, ToStringSerializer.instance);
		module.addSerializer(Long.TYPE, ToStringSerializer.instance);

		objectMapper.registerModule(module);

		return objectMapper;
	}
}
