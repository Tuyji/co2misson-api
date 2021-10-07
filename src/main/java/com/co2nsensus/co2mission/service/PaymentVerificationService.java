package com.co2nsensus.co2mission.service;

import com.co2nsensus.co2mission.model.response.verification.PaymentVerificationModel;

public interface PaymentVerificationService {
	PaymentVerificationModel verifyPayment(String affiliateId,String authorizationCode);
	
}
