package com.co2nsensus.co2mission.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.co2nsensus.co2mission.model.entity.AffiliateStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyChannelData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4269543248247164112L;
	private int monthIndex;
	private BigDecimal totalSales;
	private AffiliateStatus affiliateStatus;
	private BigDecimal earnings;
	private BigDecimal referralEarnings;
	private boolean isPaidOut;
	private MonthlySubscriptionData subscriptionData;

	public static MonthlyChannelData zeroChannelData() {
		return MonthlyChannelData.builder().earnings(new BigDecimal(0)).monthIndex(LocalDateTime.now().getMonthValue())
				.referralEarnings(new BigDecimal(0)).totalSales(new BigDecimal(0)).build();
	}
}
