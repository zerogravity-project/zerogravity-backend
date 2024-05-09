package com.zerogravity.myapp.model.dao;

import java.util.List;

import com.zerogravity.myapp.model.dto.SceneObject;

public interface SceneObjectDao {
	public abstract List<SceneObject> selectAll();
}
