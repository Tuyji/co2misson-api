package com.co2nsensus.co2mission.service;

import java.util.List;
import java.util.Optional;

import com.co2nsensus.co2mission.model.dto.OffsetProjectModel;
import com.co2nsensus.co2mission.model.entity.OffsetProject;

public interface OffsetProjectService {

	Optional<OffsetProject> getOffsetProjectByExternalId(String extternalId);
	
	OffsetProject saveOffsetProject(OffsetProject offsetProject);
	
	List<OffsetProjectModel> getOffsetProjects();
}
