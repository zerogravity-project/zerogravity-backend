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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api-zerogravity/objects")
@Tag(name = "Scene Object Management", description = "메인 3D 오브젝트 관리 API")
public class SceneObjectRestController {

	private final SceneObjectService sceneObjectService;

	@Autowired
	public SceneObjectRestController(SceneObjectService sceneObjectService) {
		this.sceneObjectService = sceneObjectService;
	}

	@GetMapping
	@Operation(summary = "메인 3D 오브젝트 전체 조회", description = "무중력 애플리케이션에서 사용 가능한 3D 오브젝트 목록을 반환합니다.")
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "200", description = "목록을 성공적으로 검색했습니다."),
			@ApiResponse(responseCode = "404", description = "찾고자 하는 리소스를 찾을 수 없습니다.") 
	})

	public ResponseEntity<?> getAllSceneObjects() {
		List<SceneObject> sceneObjects = sceneObjectService.getSceneObjectsList();

		if (sceneObjects == null || sceneObjects.size() == 0) {
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<List<SceneObject>>(sceneObjects, HttpStatus.OK);
	}
}
