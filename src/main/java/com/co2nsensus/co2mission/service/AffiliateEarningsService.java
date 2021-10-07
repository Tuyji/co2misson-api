package com.co2nsensus.co2mission.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;

import com.co2nsensus.co2mission.model.dto.MonthlyChannelData;
import com.co2nsensus.co2mission.model.entity.AffiliateStatus;
import com.co2nsensus.co2mission.model.response.earnings.EarningModel;
import com.co2nsensus.co2mission.model.response.earnings.MonthlyEarningsModel;

public interface AffiliateEarningsService {
	BigDecimal calculateAffiliateEarnings(AffiliateStatus status, BigDecimal totalTransaction);

	BigDecimal calculateAffiliateEarnings(MonthlyChannelData monthlyChannelData);

	List<MonthlyEarningsModel> getMonthlyEarningsByYear(String affiliateId, String year);

	MonthlyEarningsModel getMonthlyEarnings(String affiliateId, String year, int month);

	EarningModel getAffiliateEarningSummary(String affiliateId);

	EarningModel getPeriodEarnings(String affiliateId,
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate);
	
	Map<String,List<MonthlyChannelData>> getAffiliateMonthlyChannelDataMap(String affiliateId);
}
