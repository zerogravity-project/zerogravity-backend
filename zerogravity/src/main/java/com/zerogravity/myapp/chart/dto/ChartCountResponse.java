package com.zerogravity.myapp.chart.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Emotion Count Chart Response (for Scatter Chart)")
public class ChartCountResponse {

	@Schema(description = "Chart data list")
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

	@Schema(description = "Data point")
	public static class DataPoint {
		@Schema(description = "Label (SUN/MON/.., 1/2/.., JAN/FEB/..)", example = "MON")
		private String label;

		@Schema(description = "Position (x-axis)", example = "1.396")
		private Double position;

		@Schema(description = "Emotion ID", example = "2")
		private Integer emotionId;

		@Schema(description = "Emotion Type", example = "MID_NEGATIVE")
		private String emotionType;

		@Schema(description = "Timestamp (ISO 8601 with offset)", example = "2025-10-20T15:00:00+09:00")
		private String timestamp;

		@Schema(description = "Daily count", example = "0")
		private Integer daily;

		@Schema(description = "Moment count", example = "1")
		private Integer moment;

		@Schema(description = "Total count", example = "1")
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
