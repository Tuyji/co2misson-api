package com.co2nsensus.co2mission.service;

import javax.servlet.http.HttpServletRequest;

import com.co2nsensus.co2mission.model.request.ResetPasswordRequestModel;

public interface PasswordOperationsService {
	
	String processForgotPassword(HttpServletRequest servletRequest, String email);

    String processResetPassword(ResetPasswordRequestModel requestModel);
}
