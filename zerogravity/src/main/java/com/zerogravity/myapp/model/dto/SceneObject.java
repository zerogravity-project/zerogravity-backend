package com.zerogravity.myapp.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "3D 오브젝트 정보")
public class SceneObject {
	@Schema(description = "3D 오브젝트 ID", example = "abc123")
	private String sceneObjectId;
	
	@Schema(description = "3D 오브젝트 이름", example = "나무 모델")
	private String name;
	
	@Schema(description = "3D 오브젝트 파일 경로", example = "https://example.com/models/tree")
	private String modelUrl;
	
	@Schema(description = "생성 시간", example = "2021-10-01T12:00:00Z")
	private String createdTime;
	
	@Schema(description = "업데이트 시간", example = "2021-12-01T12:00:00Z")
	private String updatedTime;

	public SceneObject() {

	}

	public SceneObject(String sceneObjectId, String name, String modelUrl, String createdTime, String updatedTime) {
		this.sceneObjectId = sceneObjectId;
		this.name = name;
		this.modelUrl = modelUrl;
		this.createdTime = createdTime;
		this.updatedTime = updatedTime;
	}

	public String getSceneObjectId() {
		return sceneObjectId;
	}

	public void setSceneObjectId(String sceneObjectId) {
		this.sceneObjectId = sceneObjectId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getModelUrl() {
		return modelUrl;
	}

	public void setModelUrl(String modelUrl) {
		this.modelUrl = modelUrl;
	}

	public String getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}

	public String getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(String updatedTime) {
		this.updatedTime = updatedTime;
	}

	@Override
	public String toString() {
		return "SceneObject [sceneObjectId=" + sceneObjectId + ", name=" + name + ", modelUrl=" + modelUrl
				+ ", createdTime=" + createdTime + ", updatedTime=" + updatedTime + "]";
	}
}
