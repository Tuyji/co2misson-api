package com.co2nsensus.co2mission.service.impl;

import java.util.UUID;

import com.co2nsensus.co2mission.exception.AffiliateErrorCodes;
import com.co2nsensus.co2mission.exception.AffiliateNotFoundException;
import com.co2nsensus.co2mission.model.entity.Affiliate;
import com.co2nsensus.co2mission.model.response.referral.AffiliateReferralModel;
import com.co2nsensus.co2mission.repo.AffiliateRepository;
import com.co2nsensus.co2mission.service.ReferralService;
import org.springframework.stereotype.Service;

@Service
public class ReferralServiceImpl implements ReferralService {

	private final AffiliateRepository affiliateRepository;

	public ReferralServiceImpl(AffiliateRepository affiliateRepository) {
		this.affiliateRepository = affiliateRepository;
	}

	@Override
	public AffiliateReferralModel getReferralInfo(String channelId) {
		Affiliate affiliate = affiliateRepository.findById(UUID.fromString(channelId))
				.orElseThrow(() -> new AffiliateNotFoundException(AffiliateErrorCodes.AFFILIATE_NOT_FOUND.getCode(),
						AffiliateErrorCodes.AFFILIATE_NOT_FOUND.getMessage() + " channelId: " + channelId));
		
		AffiliateReferralModel referralModel = new AffiliateReferralModel();
		return referralModel;

	}

}
