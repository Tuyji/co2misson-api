package com.co2nsensus.co2mission.model.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class SubscriptionModel {
	private String subscriptionId;
	private String planId;
	private String currencyCode;
	private BigDecimal planPrice;
	private int intervalCount;
	private BigDecimal carbon;
	private int trees;
}
