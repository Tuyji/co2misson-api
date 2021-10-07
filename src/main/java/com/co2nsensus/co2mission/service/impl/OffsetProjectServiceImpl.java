package com.co2nsensus.co2mission.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.co2nsensus.co2mission.model.dto.OffsetProjectModel;
import com.co2nsensus.co2mission.model.entity.OffsetProject;
import com.co2nsensus.co2mission.repo.OffsetProjectRepository;
import com.co2nsensus.co2mission.service.OffsetProjectService;

@Service
public class OffsetProjectServiceImpl implements OffsetProjectService {

	private final OffsetProjectRepository offsetProjectRepository;

	public OffsetProjectServiceImpl(OffsetProjectRepository offsetProjectRepository) {
		this.offsetProjectRepository = offsetProjectRepository;
	}

	@Override
	public Optional<OffsetProject> getOffsetProjectByExternalId(String externalId) {
		return offsetProjectRepository.findByExternalId(externalId);
	}

	@Override
	public OffsetProject saveOffsetProject(OffsetProject offsetProject) {
		return offsetProjectRepository.save(offsetProject);
	}

	@Override
	public List<OffsetProjectModel> getOffsetProjects() {
		return offsetProjectRepository.findAll().stream()
				.map(o -> OffsetProjectModel.builder().id(o.getId().toString()).name(o.getProjectName()).build())
				.collect(Collectors.toList());
	}

}
