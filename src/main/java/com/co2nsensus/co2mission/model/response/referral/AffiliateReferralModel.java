package com.co2nsensus.co2mission.model.response.referral;

import lombok.Data;

import java.util.List;

@Data
public class AffiliateReferralModel {
	private List<ReferralInfoModel> referrals;
	private String referralCode;
}
