package com.co2nsensus.co2mission.repo.platform;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.co2nsensus.co2mission.model.entity.platform.AffiliatePlatform;

public interface AffiliatePlatformRepository extends JpaRepository<AffiliatePlatform, UUID>{

}
