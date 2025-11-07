package com.zerogravity.myapp.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Common API response wrapper for success responses
 * 
 * Example:
 * {
 *   "success": true,
 *   "data": { "emotionRecordId": "1234567890123456789" },
 *   "timestamp": "2025-10-26T15:00:00+09:00"
 * }
 */
@Schema(description = "API Success Response")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

	@Schema(description = "Success status", example = "true")
	private Boolean success;

	@Schema(description = "Response data")
	private T data;

	@Schema(description = "Timestamp (ISO 8601 with offset)", example = "2025-10-26T15:00:00+09:00")
	private String timestamp;

	public ApiResponse() {
		this.success = true;
	}

	public ApiResponse(T data, String timestamp) {
		this.success = true;
		this.data = data;
		this.timestamp = timestamp;
	}

	public ApiResponse(Boolean success, T data, String timestamp) {
		this.success = success;
		this.data = data;
		this.timestamp = timestamp;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return "ApiResponse{" +
			"success=" + success +
			", data=" + data +
			", timestamp='" + timestamp + '\'' +
			'}';
	}
}
