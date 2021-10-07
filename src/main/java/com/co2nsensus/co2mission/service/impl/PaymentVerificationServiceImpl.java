package com.co2nsensus.co2mission.service.impl;

import org.springframework.stereotype.Service;

import com.co2nsensus.co2mission.model.entity.Affiliate;
import com.co2nsensus.co2mission.model.response.verification.PaymentVerificationModel;
import com.co2nsensus.co2mission.service.AffiliateService;
import com.co2nsensus.co2mission.service.PaymentVerificationService;
import com.co2nsensus.co2mission.service.payment.paypal.PaypalInterface;

@Service
public class PaymentVerificationServiceImpl implements PaymentVerificationService {

	private final AffiliateService affiliateService;
	private final PaypalInterface paypalInterface;

	public PaymentVerificationServiceImpl(AffiliateService affiliateService, PaypalInterface paypalInterface) {
		this.affiliateService = affiliateService;
		this.paypalInterface = paypalInterface;
	}

	@Override
	public PaymentVerificationModel verifyPayment(String affiliateId, String authorizationCode) {
		PaymentVerificationModel response = paypalInterface.verifyUserPaymentInfo(affiliateId, authorizationCode);
		Affiliate affiliate = affiliateService.getAffiliateEntityById(affiliateId);
		affiliate.setPaymentVerificationStatus(response.getStatus());
		affiliateService.saveAffiliate(affiliate);
		return response;

	}

}
