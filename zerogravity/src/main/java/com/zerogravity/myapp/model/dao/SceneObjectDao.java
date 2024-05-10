package com.zerogravity.myapp.model.dao;

import java.util.List;

import com.zerogravity.myapp.model.dto.SceneObject;

public interface SceneObjectDao {
	// 전체 3D 오브젝트 조회
	public abstract List<SceneObject> selectAllSceneObject();
}
