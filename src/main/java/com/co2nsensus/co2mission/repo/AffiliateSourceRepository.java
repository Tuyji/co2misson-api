package com.co2nsensus.co2mission.repo;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.co2nsensus.co2mission.model.entity.source.AffiliateSource;

public interface AffiliateSourceRepository extends JpaRepository<AffiliateSource,UUID>{

	List<AffiliateSource> findByAffiliateId(UUID affiliateId);
}
