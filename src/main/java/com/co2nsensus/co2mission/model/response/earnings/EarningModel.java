package com.co2nsensus.co2mission.model.response.earnings;

import lombok.Data;

import java.math.BigDecimal;

import com.co2nsensus.co2mission.model.response.MonthlyStatusModel;

@Data
public class EarningModel {
	private MonthlyEarningsModel currentMonth;
	private BigDecimal totalEarnings;
	private BigDecimal lastMonthEarnings;
	private int last30DaysClicks;
	private int last30DaysPurchases;
	private int unreadTransactions;
	private boolean isPaymentEligible;
}
