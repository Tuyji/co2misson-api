package com.co2nsensus.co2mission.controller.admin;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.co2nsensus.co2mission.model.dto.OffsetProjectModel;
import com.co2nsensus.co2mission.service.OffsetProjectService;

@RestController
@RequestMapping("/admin/projects")
public class AdminProjectController {

	private final OffsetProjectService offsetProjectService;

	public AdminProjectController(OffsetProjectService offsetProjectService) {
		this.offsetProjectService = offsetProjectService;
	}

	public ResponseEntity<?> getOffsetProjects() {
		List<OffsetProjectModel> response = offsetProjectService.getOffsetProjects();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
