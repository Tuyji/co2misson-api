package com.co2nsensus.co2mission.repo.platform;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.co2nsensus.co2mission.model.entity.platform.Platform;

public interface PlatformRepository extends JpaRepository<Platform, UUID>{
}
