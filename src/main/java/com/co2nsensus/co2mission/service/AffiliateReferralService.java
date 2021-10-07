package com.co2nsensus.co2mission.service;

import java.math.BigDecimal;

import com.co2nsensus.co2mission.model.entity.Affiliate;
import com.co2nsensus.co2mission.model.response.referral.AffiliateReferralModel;

public interface AffiliateReferralService {
	BigDecimal getEarningsFromReferrers(Affiliate affiliate);
	
	AffiliateReferralModel getAffiliateReferrals(String affiliateId);
	
	
}
