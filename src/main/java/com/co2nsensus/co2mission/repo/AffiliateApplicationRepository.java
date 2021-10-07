package com.co2nsensus.co2mission.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.co2nsensus.co2mission.model.entity.AffiliateApplication;
import org.springframework.stereotype.Repository;

@Repository
public interface AffiliateApplicationRepository extends JpaRepository<AffiliateApplication, Long>{

}
