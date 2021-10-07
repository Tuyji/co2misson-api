package com.co2nsensus.co2mission.model.response.analytics;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsModel {
	private String channelId;
	private String sourceName;
	private int clickCount;
	private int addToCartCount;
	private int purchaseCount;
	@Default
	private BigDecimal conversion = new BigDecimal(0);
	@Default
	private BigDecimal revenue = new BigDecimal(0);
	private String breakdownValue;
	private String dateFrom;
	private String dateTo;
	private int month;
	private int year;
}
