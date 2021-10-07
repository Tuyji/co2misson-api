package com.co2nsensus.co2mission.model.response.subscription;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class AffiliateSubscriptionsResponseMetrics {

	private int activeSubscribers;
	private int cancelledSubscribers;
	@Builder.Default
	private BigDecimal monthlyRevenue = new BigDecimal(0);
	@Builder.Default
	private BigDecimal monthlyEarnings = new BigDecimal(0);
	@Builder.Default
	private BigDecimal revenuePerSubscriber = new BigDecimal(0);
	@Builder.Default
	private BigDecimal monthlyOffsets = new BigDecimal(0);
	private int monthlyTrees;
	@Builder.Default
	private BigDecimal totalOffsetAmount = new BigDecimal(0);
	@Builder.Default
	private BigDecimal totalSales = new BigDecimal(0);
	@Builder.Default
	private BigDecimal totalEarnings = new BigDecimal(0);
	private int totalTrees;
	
}
