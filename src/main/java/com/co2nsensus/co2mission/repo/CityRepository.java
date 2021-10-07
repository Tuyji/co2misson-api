package com.co2nsensus.co2mission.repo;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.co2nsensus.co2mission.model.entity.City;

public interface CityRepository extends JpaRepository<City, UUID>{
	
	List<City> findByCountryId(Long countryId);
}
