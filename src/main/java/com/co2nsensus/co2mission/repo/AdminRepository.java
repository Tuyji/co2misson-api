package com.co2nsensus.co2mission.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.co2nsensus.co2mission.model.entity.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long>{
	Optional<Admin> findByEmail(String email);
}
