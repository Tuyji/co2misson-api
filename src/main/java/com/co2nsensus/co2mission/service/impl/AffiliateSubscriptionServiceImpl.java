package com.co2nsensus.co2mission.service.impl;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.co2nsensus.co2mission.model.dto.AffiliateSubscriptionModel;
import com.co2nsensus.co2mission.model.dto.MonthlyChannelData;
import com.co2nsensus.co2mission.model.dto.SubscriptionUpdateMessage;
import com.co2nsensus.co2mission.model.entity.Affiliate;
import com.co2nsensus.co2mission.model.entity.AffiliateSubscription;
import com.co2nsensus.co2mission.model.enums.AffiliateSubscriptionStatus;
import com.co2nsensus.co2mission.model.response.source.AffiliateSourceModel;
import com.co2nsensus.co2mission.model.response.source.AffiliateSourceModelList;
import com.co2nsensus.co2mission.model.response.subscription.AffiliateSubscriptionsResponseMetrics;
import com.co2nsensus.co2mission.model.response.subscription.AffiliateSubscriptionsResponseModel;
import com.co2nsensus.co2mission.repo.AffiliateSubscriptionRepository;
import com.co2nsensus.co2mission.service.AffiliateEarningsService;
import com.co2nsensus.co2mission.service.AffiliateService;
import com.co2nsensus.co2mission.service.AffiliateSourceService;
import com.co2nsensus.co2mission.service.AffiliateSubscriptionService;
import com.co2nsensus.co2mission.service.RedisService;
import com.co2nsensus.co2mission.utils.DateUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AffiliateSubscriptionServiceImpl implements AffiliateSubscriptionService {

	private final AffiliateSubscriptionRepository affiliateSubscriptionRepository;
	private final RedisService redisService;
	private final AffiliateService affiliateService;
	private final AffiliateEarningsService affiliateEarningsService;
	private final AffiliateSourceService affiliateSourceService;

	public AffiliateSubscriptionServiceImpl(AffiliateSubscriptionRepository affiliateSubscriptionRepository,
			RedisService redisService, AffiliateService affiliateService,
			AffiliateEarningsService affiliateEarningsService, AffiliateSourceService affiliateSourceService) {
		this.affiliateSubscriptionRepository = affiliateSubscriptionRepository;
		this.redisService = redisService;
		this.affiliateService = affiliateService;
		this.affiliateEarningsService = affiliateEarningsService;
		this.affiliateSourceService = affiliateSourceService;
	}

	@Override
	public void updateAffiliateSubscription(SubscriptionUpdateMessage subscriptionUpdateMessage) {
		log.info("AffiliateSubscriptionServiceImpl.getAffiliateSubscribers started");

		AffiliateSubscription affiliateSubscriptionEntity = new AffiliateSubscription();
		BeanUtils.copyProperties(subscriptionUpdateMessage, affiliateSubscriptionEntity);
		affiliateSubscriptionRepository.save(affiliateSubscriptionEntity);

		log.info("AffiliateSubscriptionServiceImpl.getAffiliateSubscribers finished");
	}

	@Override
	public AffiliateSubscriptionsResponseModel getAffiliateSubscribers(String id, String source) {
		log.info("AffiliateServiceImpl.getAffiliateSubscribers started");

		Affiliate affiliate = affiliateService.getAffiliateEntityById(id);

		// Filter affiliate subscriptions by source and status
		List<AffiliateSubscription> affSubscriptions = affiliate.getSubscriptions().stream()
				// .filter(s -> s.getSubscriptionStatus() == AffiliateSubscriptionStatus.ACTIVE)
				.filter(s -> StringUtils.isAllBlank(source) || s.getSource().equals(source))
				.collect(Collectors.toList());

		AffiliateSubscriptionsResponseMetrics metrics = AffiliateSubscriptionsResponseMetrics.builder().build();
		for (AffiliateSubscription subscription : affSubscriptions) {
			if (subscription.getSubscriptionStatus() == AffiliateSubscriptionStatus.ACTIVE) {
				metrics.setActiveSubscribers(metrics.getActiveSubscribers() + 1);
				metrics.setMonthlyRevenue(metrics.getMonthlyRevenue().add(subscription.getMonthlyPaymentDollar()));
				metrics.setMonthlyOffsets(metrics.getMonthlyOffsets().add(subscription.getMonthlyOffset()));
				metrics.setMonthlyTrees(metrics.getMonthlyTrees() + subscription.getMonthlyTrees());
			} else {
				metrics.setCancelledSubscribers(metrics.getCancelledSubscribers() + 1);
			}
		}
		AffiliateSourceModelList affiliateSourceList = affiliateSourceService.getAllAffiliateSources(id);
		MonthlyChannelData monthlyChannelData = redisService.getMonthlySales(affiliate.getId().toString(),
				String.valueOf(DateUtil.getCurrentYear()), DateUtil.getCurrentMonth());
		metrics.setMonthlyEarnings(
				metrics.getMonthlyRevenue().multiply(monthlyChannelData.getAffiliateStatus().getComissionRate()));
		if (metrics.getActiveSubscribers() > 0)
			metrics.setRevenuePerSubscriber(
					metrics.getTotalSales().divide(BigDecimal.valueOf(metrics.getActiveSubscribers())));

		Map<String, List<MonthlyChannelData>> affiliateMontlyChannelMap = affiliateEarningsService
				.getAffiliateMonthlyChannelDataMap(id);

		List<MonthlyChannelData> allChannelData = affiliateMontlyChannelMap.values().stream().flatMap(List::stream)
				.collect(Collectors.toList());

		for (MonthlyChannelData channelData : allChannelData) {
			metrics.setTotalOffsetAmount(
					metrics.getTotalOffsetAmount().add(channelData.getSubscriptionData().getTotalOffset()));
			metrics.setTotalTrees(metrics.getTotalTrees() + channelData.getSubscriptionData().getTreesPlanted());
			metrics.setTotalSales(metrics.getTotalSales().add(channelData.getSubscriptionData().getTotalSales()));
			metrics.setTotalEarnings(metrics.getTotalEarnings().add(channelData.getSubscriptionData().getTotalSales()
					.multiply(channelData.getAffiliateStatus().getComissionRate())));
		}

		List<AffiliateSubscriptionModel> affiliateSubscriptionModels = affSubscriptions.stream()
				.map(s -> convert(s, affiliateSourceList)).collect(Collectors.toList());

		AffiliateSubscriptionsResponseModel responseModel = new AffiliateSubscriptionsResponseModel();
		responseModel.setAffiliateSubscriptionModels(affiliateSubscriptionModels);
		responseModel.setAffiliateSubscriptionsResponseMetrics(metrics);

		log.info("AffiliateServiceImpl.getAffiliateSubscribers finished");

		return responseModel;
	}

	private AffiliateSubscriptionModel convert(AffiliateSubscription affiliateSubscription,
			AffiliateSourceModelList sourceModelList) {
		Optional<AffiliateSourceModel> sourceModel = sourceModelList.getSourceList().stream()
				.filter(s -> s.getAnalyticsName().equals(affiliateSubscription.getSource())).findFirst();
		String sourceName = "";
		if (sourceModel.isPresent()) {
			sourceName = sourceModel.get().getName();
		} else {
			sourceName = affiliateSubscription.getSource();
		}
		return AffiliateSubscriptionModel.builder().monthlyCharge(affiliateSubscription.getMonthlyPaymentDollar())
				.monthlyOffsetAmount(affiliateSubscription.getMonthlyOffset())
				.monthlyTrees(affiliateSubscription.getMonthlyTrees()).source(sourceName)
				.status(affiliateSubscription.getSubscriptionStatus())
				.subscriptionDate(affiliateSubscription.getCreatedAt().format(DateTimeFormatter.ISO_DATE))
				.subscriptionType(affiliateSubscription.getName())
				.subscriberName(affiliateSubscription.getSubscriberName())
				.subscriberSurname(affiliateSubscription.getSubscriberSurname()).build();
	}

//	private AffiliateSubscription convert(SubscriptionUpdateMessage subscriptionUpdateMessage) {
//		AffiliateSubscription affiliateSubscription = affiliateSubscriptionRepository
//				.findBySubscriptionId(subscriptionUpdateMessage.getSubscriptionId()).orElseThrow(
//						() -> new SubscriptionNotFoundException(AffiliateErrorCodes.SUBSCRIPTION_NOT_FOUND.getCode(),
//								AffiliateErrorCodes.SUBSCRIPTION_NOT_FOUND.getMessage()));
//		if (subscriptionUpdateMessage.getAction() == SubscriptionMessageActionType.cancelled) {
//			affiliateSubscription.setSubscriptionStatus(AffiliateSubscriptionStatus.DELETED);
//		} else {
//			affiliateSubscription.setMonthlyOffset(subscriptionUpdateMessage.getCarbon());
//			affiliateSubscription.setMonthlyPayment(subscriptionUpdateMessage.getPlanPrice());
//			affiliateSubscription.setMonthlyPaymentCurrency(subscriptionUpdateMessage.getCurrencyCode());
//		}
//		return affiliateSubscription;
//	}

}
