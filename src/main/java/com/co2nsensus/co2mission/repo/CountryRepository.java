package com.co2nsensus.co2mission.repo;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.co2nsensus.co2mission.model.entity.Country;

public interface CountryRepository extends JpaRepository<Country,UUID>{

}
