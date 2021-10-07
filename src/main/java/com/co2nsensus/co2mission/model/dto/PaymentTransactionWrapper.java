package com.co2nsensus.co2mission.model.dto;

import java.math.BigDecimal;

import com.co2nsensus.co2mission.model.entity.PaymentTransaction;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentTransactionWrapper{

	private PaymentTransaction paymentTransaction;
	private String sourceName;
	private BigDecimal earning;
}
