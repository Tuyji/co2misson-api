package com.co2nsensus.co2mission.repo;

import java.util.Optional;

import com.co2nsensus.co2mission.model.entity.ResetPasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ResetPasswordTokenRepository extends JpaRepository<ResetPasswordToken, Integer>{

    @Transactional(readOnly = true)
    public Optional<ResetPasswordToken> findByToken(@Param("token") String token);

 }
