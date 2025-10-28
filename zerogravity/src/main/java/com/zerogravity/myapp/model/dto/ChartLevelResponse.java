package com.zerogravity.myapp.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "감정 레벨 차트 응답")
public class ChartLevelResponse {

	@Schema(description = "차트 데이터 목록")
	private List<DataPoint> data;

	@Schema(description = "평균 감정 레벨", example = "4.14")
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

	@Schema(description = "데이터 포인트")
	public static class DataPoint {
		@Schema(description = "라벨 (SUN/MON/.., 1/2/.., JAN/FEB/..)", example = "MON")
		private String label;

		@Schema(description = "값 (평균 감정 레벨)", example = "3.8")
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
