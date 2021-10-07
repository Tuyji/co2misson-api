package com.co2nsensus.co2mission.repo;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.co2nsensus.co2mission.model.entity.OffsetProject;

public interface OffsetProjectRepository extends JpaRepository<OffsetProject, UUID>{

	Optional<OffsetProject> findByExternalId(String externalId);
}
