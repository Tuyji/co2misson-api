package com.co2nsensus.co2mission.repo;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.co2nsensus.co2mission.model.entity.AffiliateStatus;
import com.co2nsensus.co2mission.model.enums.AffiliateStatusType;

public interface AffiliateStatusRepository extends JpaRepository<AffiliateStatus, UUID>{
	AffiliateStatus findByAffiliateStatusType(AffiliateStatusType type);
}
