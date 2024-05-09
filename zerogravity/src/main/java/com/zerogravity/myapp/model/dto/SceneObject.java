package com.zerogravity.myapp.model.dto;

public class SceneObject {
	private String sceneObjectId;
	private String name;
	private String modelUrl;
	private String createdTime;
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
