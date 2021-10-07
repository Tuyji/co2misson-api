package com.co2nsensus.co2mission.service.impl;

import com.co2nsensus.co2mission.logging.Loggable;
import com.co2nsensus.co2mission.model.entity.Affiliate;
import com.co2nsensus.co2mission.model.entity.ResetPasswordToken;
import com.co2nsensus.co2mission.repo.ResetPasswordTokenRepository;
import com.co2nsensus.co2mission.service.ResetPasswordTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
@Slf4j
public class ResetPasswordTokenServiceImpl implements ResetPasswordTokenService {

    private final ResetPasswordTokenRepository resetPasswordTokenRepository;

    public ResetPasswordTokenServiceImpl(ResetPasswordTokenRepository resetPasswordTokenRepository){
        this.resetPasswordTokenRepository = resetPasswordTokenRepository;
    }

    @Override
    public ResetPasswordToken findByToken(String token) {
        log.info("ResetPasswordTokenServiceImpl.findByToken started");

        ResetPasswordToken resetPasswordToken = resetPasswordTokenRepository.findByToken(token).orElse(null);

        log.info("ResetPasswordTokenServiceImpl.findByToken finished");

        return resetPasswordToken;
    }

    @Override
    public ResetPasswordToken createAndSaveResetPasswordToken(Affiliate affiliate) {
        log.info("ResetPasswordTokenServiceImpl.createAndSaveResetPasswordToken started");

        ResetPasswordToken resetToken = new ResetPasswordToken();
        resetToken.setAffiliate(affiliate);
        Date createdDate = new Date();
        resetToken.setToken(UUID.randomUUID().toString());
        resetToken.setCreatedDate(createdDate);
        resetPasswordTokenRepository.save(resetToken);

        log.info("ResetPasswordTokenServiceImpl.createAndSaveResetPasswordToken finished");

        return resetToken;
    }
}
