package com.co2nsensus.co2mission.model.response.verification;

import com.co2nsensus.co2mission.model.enums.PaymentVerificationStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentVerificationModel {
	private PaymentVerificationStatus status;
	
}
