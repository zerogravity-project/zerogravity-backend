package com.zerogravity.myapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zerogravity.myapp.model.dto.SceneObject;
import com.zerogravity.myapp.model.service.SceneObjectService;

@RestController
@RequestMapping("/api-zerogravity/objects")
public class SceneObjectRestController {

	private final SceneObjectService sceneObjectService;

	@Autowired
	public SceneObjectRestController(SceneObjectService sceneObjectService) {
		this.sceneObjectService = sceneObjectService;
	}

	@GetMapping
	public ResponseEntity<?> getAllSceneObjects() {
		List<SceneObject> sceneObjects = sceneObjectService.getSceneObjectsList();

		if (sceneObjects == null || sceneObjects.size() == 0) {
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<List<SceneObject>>(sceneObjects, HttpStatus.OK);
	}
}
