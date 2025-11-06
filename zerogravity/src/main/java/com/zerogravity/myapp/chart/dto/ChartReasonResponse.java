package com.zerogravity.myapp.chart.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Emotion Reason Chart Response")
public class ChartReasonResponse {

	@Schema(description = "Chart data list")
	private List<DataPoint> data;

	public ChartReasonResponse() {}

	public ChartReasonResponse(List<DataPoint> data) {
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
		@Schema(description = "Label (emotion reason)", example = "Work")
		private String label;

		@Schema(description = "Count", example = "5")
		private Integer count;

		public DataPoint() {}

		public DataPoint(String label, Integer count) {
			this.label = label;
			this.count = count;
		}

		public String getLabel() {
			return label;
		}

		public void setLabel(String label) {
			this.label = label;
		}

		public Integer getCount() {
			return count;
		}

		public void setCount(Integer count) {
			this.count = count;
		}
	}
}
