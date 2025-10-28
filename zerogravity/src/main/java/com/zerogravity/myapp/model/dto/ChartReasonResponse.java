package com.zerogravity.myapp.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "감정 이유 차트 응답")
public class ChartReasonResponse {

	@Schema(description = "차트 데이터 목록")
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

	@Schema(description = "데이터 포인트")
	public static class DataPoint {
		@Schema(description = "라벨 (감정 이유)", example = "Work")
		private String label;

		@Schema(description = "카운트", example = "5")
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
