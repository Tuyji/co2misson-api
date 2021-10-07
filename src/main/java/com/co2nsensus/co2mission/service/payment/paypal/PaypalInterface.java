package com.co2nsensus.co2mission.service.payment.paypal;

import com.co2nsensus.co2mission.model.response.verification.PaymentVerificationModel;

public interface PaypalInterface {
	PaymentVerificationModel verifyUserPaymentInfo(String affiliateId, String authorizationCode);
}
