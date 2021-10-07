package com.co2nsensus.co2mission.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.co2nsensus.co2mission.model.dto.MonthlyChannelData;
import com.co2nsensus.co2mission.model.entity.AffiliateStatus;
import com.co2nsensus.co2mission.model.enums.AffiliateStatusType;
import com.co2nsensus.co2mission.model.response.MonthlyStatusModel;
import com.co2nsensus.co2mission.model.response.analytics.AnalyticsModel;
import com.co2nsensus.co2mission.model.response.earnings.EarningModel;
import com.co2nsensus.co2mission.model.response.earnings.MonthlyEarningsModel;
import com.co2nsensus.co2mission.model.response.transaction.TransactionHistoryResponseModel;
import com.co2nsensus.co2mission.service.AffiliateEarningsService;
import com.co2nsensus.co2mission.service.AffiliateService;
import com.co2nsensus.co2mission.service.AffiliateStatusService;
import com.co2nsensus.co2mission.service.AffiliateTransactionService;
import com.co2nsensus.co2mission.service.AnalyticsService;
import com.co2nsensus.co2mission.service.RedisService;

@Service
public class AffiliateEarningsServiceImpl implements AffiliateEarningsService {

	private final RedisService redisService;
	private final AnalyticsService analyticsService;
	private final AffiliateStatusService affiliateStatusService;
	private final AffiliateTransactionService affiliateTransactionService;
	private final AffiliateService affiliateService;

	@Value("${app.transaction.start-year}")
	private String startYear;

	public AffiliateEarningsServiceImpl(RedisService redisService, AnalyticsService analyticsService,
			AffiliateStatusService affiliateStatusService, AffiliateTransactionService affiliateTransactionService,
			AffiliateService affiliateService) {
		this.redisService = redisService;
		this.analyticsService = analyticsService;
		this.affiliateStatusService = affiliateStatusService;
		this.affiliateTransactionService = affiliateTransactionService;
		this.affiliateService = affiliateService;
	}

	@Override
	public BigDecimal calculateAffiliateEarnings(AffiliateStatus status, BigDecimal totalTransaction) {
		return status.getComissionRate().multiply(totalTransaction);
	}

	@Override
	public BigDecimal calculateAffiliateEarnings(MonthlyChannelData monthlyChannelData) {
		return monthlyChannelData.getAffiliateStatus().getComissionRate().multiply(monthlyChannelData.getTotalSales());
	}

	@Override
	public List<MonthlyEarningsModel> getMonthlyEarningsByYear(String affiliateId, String year) {
		List<MonthlyEarningsModel> modelList = new ArrayList<>();
		redisService.getMonthlySales(affiliateId, year).stream().forEach(data -> modelList.add(convert(data)));
		return modelList;
	}

	@Override
	public MonthlyEarningsModel getMonthlyEarnings(String affiliateId, String year, int month) {
		return convert(redisService.getMonthlySales(affiliateId, year, month));
	}

	private MonthlyEarningsModel convert(MonthlyChannelData channelData) {
		AffiliateStatus affiliateStatus = affiliateStatusService.getAffiliateStatus(AffiliateStatusType.BRONZE);
		if (channelData.getAffiliateStatus() != null)
			affiliateStatus = channelData.getAffiliateStatus();

		return MonthlyEarningsModel.builder().earnings(channelData.getEarnings().setScale(2,RoundingMode.HALF_UP))
				.status(MonthlyStatusModel.builder().comissionRate(affiliateStatus.getComissionRate())
						.lowerLimit(affiliateStatus.getLowerLimit()).upperLimit(affiliateStatus.getUpperLimit())
						.statusName(affiliateStatus.getAffiliateStatusType().name()).build())
				.totalSales(channelData.getTotalSales().setScale(2,RoundingMode.HALF_UP)).month(String.valueOf(channelData.getMonthIndex())).build();
	}

	@Override
	public EarningModel getAffiliateEarningSummary(String affiliateId) {
		LocalDateTime now = LocalDateTime.now();
		List<MonthlyChannelData> monthlyData = redisService.getMonthlySales(affiliateId, String.valueOf(now.getYear()));
		MonthlyChannelData thisMonth = monthlyData.stream().filter(data -> data.getMonthIndex() == now.getMonthValue())
				.findFirst().orElse(MonthlyChannelData.zeroChannelData());
		MonthlyChannelData lastMonth = monthlyData.stream()
				.filter(data -> data.getMonthIndex() == now.getMonthValue() - 1).findFirst()
				.orElse(new MonthlyChannelData());
		EarningModel earning = new EarningModel();
		MonthlyEarningsModel currentMonth = convert(thisMonth);
		currentMonth.setMonth(LocalDateTime.now().getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH));
		currentMonth.setYear(LocalDateTime.now().getYear());
		earning.setCurrentMonth(currentMonth);

		AnalyticsModel analytics = analyticsService
				.getClickAndPurchaseCounts(Arrays.asList(affiliateService.getAffiliateEntityById(affiliateId)),
						now.minusDays(30), now,"")
				.get(affiliateId);
		earning.setLast30DaysClicks(analytics.getClickCount());
		earning.setLast30DaysPurchases(analytics.getPurchaseCount());

		earning.setLastMonthEarnings(lastMonth.getEarnings());
		earning.setTotalEarnings(
				monthlyData.stream().map(x -> x.getEarnings()).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2,RoundingMode.HALF_UP));
		return earning;
	}

	@Override
	public EarningModel getPeriodEarnings(String affiliateId, LocalDate startDate, LocalDate endDate) {
		TransactionHistoryResponseModel transactionHistoryResponseModel = affiliateTransactionService
				.getTransactionHistory(affiliateId, "", startDate, endDate, 0);
		EarningModel earning = new EarningModel();
		earning.setCurrentMonth(MonthlyEarningsModel.builder()
				.earnings(transactionHistoryResponseModel.getPaymentTransactionMetrics().getMyEarnings())
				.totalSales(transactionHistoryResponseModel.getPaymentTransactionMetrics().getTotalSalesAmount())
				.build());

		return earning;
	}

	@Override
	public Map<String, List<MonthlyChannelData>> getAffiliateMonthlyChannelDataMap(String affiliateId) {
		int start = Integer.parseInt(startYear);
		int yearNow = LocalDateTime.now().getYear();
		Map<String, List<MonthlyChannelData>> affiliateMonthlyDataMap = new HashMap<>();
		while (start <= yearNow) {
			List<MonthlyChannelData> monthlyChannelDataList = new ArrayList<>();
			monthlyChannelDataList.addAll(redisService.getMonthlySales(affiliateId, String.valueOf(start)));
			affiliateMonthlyDataMap.put(String.valueOf(start), monthlyChannelDataList);
			start++;
		}
		return affiliateMonthlyDataMap;
	}
}
