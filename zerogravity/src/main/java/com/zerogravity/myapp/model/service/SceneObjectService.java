package com.zerogravity.myapp.model.service;

import java.util.List;

import com.zerogravity.myapp.model.dto.SceneObject;

public interface SceneObjectService {
	// 전체 3D 오브젝트 조회
	public abstract List<SceneObject> getSceneObjectsList();
}
