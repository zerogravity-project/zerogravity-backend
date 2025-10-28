package com.zerogravity.myapp.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "감정 개수 차트 응답 (Scatter Chart용)")
public class ChartCountResponse {

	@Schema(description = "차트 데이터 목록")
	private List<DataPoint> data;

	public ChartCountResponse() {}

	public ChartCountResponse(List<DataPoint> data) {
		this.data = data;
	}

	public List<DataPoint> getData() {
		return data;
	}

	public void setData(List<DataPoint> data) {
		this.data = data;
	}

	@Schema(description = "데이터 포인트")
	public static class DataPoint {
		@Schema(description = "라벨 (SUN/MON/.., 1/2/.., JAN/FEB/..)", example = "MON")
		private String label;

		@Schema(description = "포지션 (x축)", example = "1.396")
		private Double position;

		@Schema(description = "감정 ID", example = "2")
		private Integer emotionId;

		@Schema(description = "감정 타입", example = "MID_NEGATIVE")
		private String emotionType;

		@Schema(description = "타임스탬프 (ISO 8601 with offset)", example = "2025-10-20T15:00:00+09:00")
		private String timestamp;

		@Schema(description = "Daily 개수", example = "0")
		private Integer daily;

		@Schema(description = "Moment 개수", example = "1")
		private Integer moment;

		@Schema(description = "총 개수", example = "1")
		private Integer total;

		public DataPoint() {}

		public DataPoint(String label, Double position, Integer emotionId, String emotionType, String timestamp,
		                 Integer daily, Integer moment, Integer total) {
			this.label = label;
			this.position = position;
			this.emotionId = emotionId;
			this.emotionType = emotionType;
			this.timestamp = timestamp;
			this.daily = daily;
			this.moment = moment;
			this.total = total;
		}

		public String getLabel() {
			return label;
		}

		public void setLabel(String label) {
			this.label = label;
		}

		public Double getPosition() {
			return position;
		}

		public void setPosition(Double position) {
			this.position = position;
		}

		public Integer getEmotionId() {
			return emotionId;
		}

		public void setEmotionId(Integer emotionId) {
			this.emotionId = emotionId;
		}

		public String getEmotionType() {
			return emotionType;
		}

		public void setEmotionType(String emotionType) {
			this.emotionType = emotionType;
		}

		public String getTimestamp() {
			return timestamp;
		}

		public void setTimestamp(String timestamp) {
			this.timestamp = timestamp;
		}

		public Integer getDaily() {
			return daily;
		}

		public void setDaily(Integer daily) {
			this.daily = daily;
		}

		public Integer getMoment() {
			return moment;
		}

		public void setMoment(Integer moment) {
			this.moment = moment;
		}

		public Integer getTotal() {
			return total;
		}

		public void setTotal(Integer total) {
			this.total = total;
		}
	}
}
