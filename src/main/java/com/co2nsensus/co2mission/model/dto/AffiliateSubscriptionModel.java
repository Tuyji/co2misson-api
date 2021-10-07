package com.co2nsensus.co2mission.model.dto;

import java.math.BigDecimal;

import com.co2nsensus.co2mission.model.enums.AffiliateSubscriptionStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AffiliateSubscriptionModel {
	private String id;
	private String channelId;
	private String subscriptionType;
	private int monthlyTrees;
	private BigDecimal monthlyOffsetAmount;
	private String source;
	private String subscriptionDate;
	private BigDecimal monthlyCharge;
	private String currency;
	private AffiliateSubscriptionStatus status;
	private String subscriberName;
	private String subscriberSurname;
}
