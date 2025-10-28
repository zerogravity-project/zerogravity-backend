package com.zerogravity.myapp.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 성공 응답을 위한 공통 API 응답 래퍼
 *
 * 예시:
 * {
 *   "success": true,
 *   "data": { "emotionRecordId": "1234567890123456789" },
 *   "timestamp": "2025-10-26T15:00:00+09:00"
 * }
 */
@Schema(description = "API 성공 응답")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

	@Schema(description = "성공 여부", example = "true")
	private Boolean success;

	@Schema(description = "응답 데이터")
	private T data;

	@Schema(description = "타임스탐프 (ISO 8601 with offset)", example = "2025-10-26T15:00:00+09:00")
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
