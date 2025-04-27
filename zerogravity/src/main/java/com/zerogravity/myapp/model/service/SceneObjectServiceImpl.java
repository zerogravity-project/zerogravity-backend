package com.zerogravity.myapp.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zerogravity.myapp.model.dao.SceneObjectDao;
import com.zerogravity.myapp.model.dto.SceneObject;

@Service
public class SceneObjectServiceImpl implements SceneObjectService {
	private final SceneObjectDao sceneObjectDao;
	
	@Autowired
	public SceneObjectServiceImpl(SceneObjectDao sceneObjectDao) {
		this.sceneObjectDao = sceneObjectDao;
	}

	@Override
	public List<SceneObject> getSceneObjectsList() {
		return sceneObjectDao.selectAllSceneObject();
	}
}
