package com.co2nsensus.co2mission.service;

import com.co2nsensus.co2mission.model.response.referral.AffiliateReferralModel;

public interface ReferralService {
	AffiliateReferralModel getReferralInfo(String channelId);
}
