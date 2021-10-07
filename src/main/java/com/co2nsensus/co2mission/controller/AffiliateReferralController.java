package com.co2nsensus.co2mission.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.co2nsensus.co2mission.model.response.referral.AffiliateReferralModel;
import com.co2nsensus.co2mission.service.AffiliateReferralService;

@RestController
@RequestMapping("/affiliate/{affiliateId}/referrals/")
public class AffiliateReferralController {

	private final AffiliateReferralService affiliateReferralService;

	public AffiliateReferralController(AffiliateReferralService affiliateReferralService) {
		this.affiliateReferralService = affiliateReferralService;
	}

	@GetMapping
	public ResponseEntity<?> getAffiliateReferrals(@PathVariable String affiliateId) {
		AffiliateReferralModel affiliateReferralModel = affiliateReferralService.getAffiliateReferrals(affiliateId);
		return new ResponseEntity<>(affiliateReferralModel, HttpStatus.OK);
	}

}
