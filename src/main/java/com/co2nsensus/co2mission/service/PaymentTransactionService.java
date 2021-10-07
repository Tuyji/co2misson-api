package com.co2nsensus.co2mission.service;

import com.co2nsensus.co2mission.model.dto.PaymentTransactionModel;
import com.co2nsensus.co2mission.model.entity.Affiliate;
import com.co2nsensus.co2mission.model.entity.PaymentTransaction;

public interface PaymentTransactionService {
	PaymentTransaction createPaymentTransaction(PaymentTransactionModel paymentTransactionModel);
	
	public void handleSubscription(PaymentTransactionModel paymentTransactionModel, PaymentTransaction paymentTransaction);
}
