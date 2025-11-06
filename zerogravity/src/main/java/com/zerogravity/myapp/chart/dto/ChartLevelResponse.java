package com.zerogravity.myapp.chart.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Emotion Level Chart Response")
public class ChartLevelResponse {

	@Schema(description = "Chart data list")
	private List<DataPoint> data;

	@Schema(description = "Average emotion level", example = "4.14")
	private Double average;

	public ChartLevelResponse() {}

	public ChartLevelResponse(List<DataPoint> data, Double average) {
		this.data = data;
		this.average = average;
	}

	public List<DataPoint> getData() {
		return data;
	}

	public void setData(List<DataPoint> data) {
		this.data = data;
	}

	public Double getAverage() {
		return average;
	}

	public void setAverage(Double average) {
		this.average = average;
	}

	@Schema(description = "Data point")
	public static class DataPoint {
		@Schema(description = "Label (SUN/MON/.., 1/2/.., JAN/FEB/..)", example = "MON")
		private String label;

		@Schema(description = "Value (average emotion level)", example = "3.8")
		private Double value;

		public DataPoint() {}

		public DataPoint(String label, Double value) {
			this.label = label;
			this.value = value;
		}

		public String getLabel() {
			return label;
		}

		public void setLabel(String label) {
			this.label = label;
		}

		public Double getValue() {
			return value;
		}

		public void setValue(Double value) {
			this.value = value;
		}
	}
}
