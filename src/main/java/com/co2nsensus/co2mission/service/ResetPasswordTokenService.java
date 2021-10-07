package com.co2nsensus.co2mission.service;

import com.co2nsensus.co2mission.model.entity.Affiliate;
import com.co2nsensus.co2mission.model.entity.ResetPasswordToken;

public interface ResetPasswordTokenService {

    public ResetPasswordToken findByToken(String token);

    ResetPasswordToken createAndSaveResetPasswordToken(Affiliate affiliate);
}
