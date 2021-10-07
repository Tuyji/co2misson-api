package com.co2nsensus.co2mission.service.impl;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.co2nsensus.co2mission.exception.AffiliateNotFoundException;
import com.co2nsensus.co2mission.exception.ResetPasswordTokenInvalid;
import com.co2nsensus.co2mission.model.request.ResetPasswordRequestModel;
import com.co2nsensus.co2mission.model.entity.Affiliate;
import com.co2nsensus.co2mission.model.entity.ResetPasswordToken;
import com.co2nsensus.co2mission.service.PasswordOperationsService;

@Service
public class PasswordOperationsServiceImpl implements PasswordOperationsService{

	@Override
	public String processForgotPassword(HttpServletRequest servletRequest, String email) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String processResetPassword(ResetPasswordRequestModel requestModel) {
		// TODO Auto-generated method stub
		return null;
	}
	
//	private final AffilateRepository affilateRepository;
//	private final 
//	
//	@Override
//	public String processForgotPassword(HttpServletRequest servletRequest, String email) {
//		log.info("AffiliateServiceImpl.processForgotPassword started");
//
//		Affiliate affiliate = affiliateRepository.findByEmail(email)
//				.orElseThrow(() -> new AffiliateNotFoundException("Affiliate not found! e-mail: " + email));
//
//		ResetPasswordToken resetPasswordToken = resetPasswordTokenService.createAndSaveResetPasswordToken(affiliate);
//
//		emailSenderService.sendEmail(servletRequest, resetPasswordToken);
//
//		StringBuilder sb = new StringBuilder();
//		sb.append("An e-mail has been sent to the addres you have provided.");
//		sb.append("Please follow the link in the e-mail to complete you password reset request.");
//
//		log.info("AffiliateServiceImpl.processForgotPassword finished");
//
//		return sb.toString();
//	}
//
//	@Override
//	public String processResetPassword(ResetPasswordRequestModel requestModel) {
//		log.info("AffiliateServiceImpl.processResetPassword started");
//
//		ResetPasswordToken resetPasswordToken = resetPasswordTokenService.findByToken(requestModel.getToken());
//
//		if (Objects.isNull(resetPasswordToken))
//			throw new ResetPasswordTokenInvalid("Invalid link, reset password error.");
//
//		if (resetPasswordToken.isExpired(resetPasswordTokenExpiration))
//			throw new ResetPasswordTokenInvalid("Expired link, reset password error.");
//
//		Affiliate affiliate = resetPasswordToken.getAffiliate();
//		affiliate.setPassword(requestModel.getPassword());
//		affiliateRepository.save(affiliate);
//
//		log.info("AffiliateServiceImpl.processResetPassword finished");
//
//		return "Password has been changed";
//	}

}
