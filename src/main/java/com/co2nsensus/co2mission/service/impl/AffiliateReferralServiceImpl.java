package com.co2nsensus.co2mission.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.co2nsensus.co2mission.model.dto.MonthlyChannelData;
import com.co2nsensus.co2mission.model.entity.Affiliate;
import com.co2nsensus.co2mission.model.entity.PayoutTransaction;
import com.co2nsensus.co2mission.model.response.referral.AffiliateReferralModel;
import com.co2nsensus.co2mission.model.response.referral.ReferralInfoModel;
import com.co2nsensus.co2mission.repo.PayoutTransactionRepository;
import com.co2nsensus.co2mission.service.AffiliateEarningsService;
import com.co2nsensus.co2mission.service.AffiliateReferralService;
import com.co2nsensus.co2mission.service.AffiliateService;

@Service
public class AffiliateReferralServiceImpl implements AffiliateReferralService {

	private final AffiliateService affiliateService;
	private final AffiliateEarningsService affiliateEarningService;
	private final PayoutTransactionRepository payoutTransactionRepository;
	private final static BigDecimal REFERRAL_PAYOUT_PERCENTAGE = new BigDecimal(0.02);

	public AffiliateReferralServiceImpl(AffiliateService affiliateService,
			PayoutTransactionRepository payoutTransactionRepository, AffiliateEarningsService affiliateEarningService) {
		this.affiliateService = affiliateService;
		this.payoutTransactionRepository = payoutTransactionRepository;
		this.affiliateEarningService = affiliateEarningService;
	}

	@Override
	public BigDecimal getEarningsFromReferrers(Affiliate affiliate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AffiliateReferralModel getAffiliateReferrals(String affiliateId) {
		Affiliate affiliate = affiliateService.getAffiliateEntityById(affiliateId);
		List<ReferralInfoModel> referrals = new ArrayList<>();
		for (Affiliate referral : affiliate.getReferrals()) {
			Map<String, List<MonthlyChannelData>> affiliateMontlyChannelMap = affiliateEarningService
					.getAffiliateMonthlyChannelDataMap(referral.getId().toString());
			List<MonthlyChannelData> allChannelData = affiliateMontlyChannelMap.values().stream().flatMap(List::stream)
					.collect(Collectors.toList());
			BigDecimal pendingPayout = new BigDecimal(0);
			BigDecimal totalPayout = new BigDecimal(0);
			BigDecimal turnover = new BigDecimal(0);

			for (MonthlyChannelData channelData : allChannelData) {
				turnover = turnover.add(channelData.getTotalSales());
				BigDecimal payout = channelData.getTotalSales().multiply(REFERRAL_PAYOUT_PERCENTAGE);
				if (channelData.isPaidOut()) {
					totalPayout = totalPayout.add(channelData.getTotalSales().multiply(REFERRAL_PAYOUT_PERCENTAGE));
				} else {
					pendingPayout = pendingPayout.add(payout);
				}

			}

			referrals.add(
					ReferralInfoModel.builder().affiliateName(referral.getFirstName() + " " + referral.getLastName())
							.pendingPayout(pendingPayout).totalPayout(totalPayout).turnover(turnover).build());
		}
		AffiliateReferralModel response = new AffiliateReferralModel();
		response.setReferralCode(affiliate.getReferrerCode());
		response.setReferrals(referrals);
		return response;
	}

	private LocalDateTime getLastPayout(Affiliate affiliate) {
		List<PayoutTransaction> payouts = payoutTransactionRepository
				.findAllByAffiliateIdOrderByCreatedAtDesc(affiliate.getId());
		if (payouts.isEmpty())
			return null;

		return payouts.get(0).getCreatedAt();
	}

}
