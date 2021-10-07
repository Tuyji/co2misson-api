package com.co2nsensus.co2mission.service;

import com.co2nsensus.co2mission.model.entity.ResetPasswordToken;

import javax.servlet.http.HttpServletRequest;

public interface EmailSenderService {

    void sendEmail(HttpServletRequest servletRequest, ResetPasswordToken resetPasswordToken);

}
