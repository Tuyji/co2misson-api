package com.co2nsensus.co2mission.controller.admin;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.co2nsensus.co2mission.model.response.referral.AffiliateReferralModel;
import com.co2nsensus.co2mission.service.AffiliateReferralService;

@RestController
@RequestMapping("/admin/referrals")
public class AdminReferralController {

	private final AffiliateReferralService affiliateReferralService;

	public AdminReferralController (AffiliateReferralService affiliateReferralService) {
		this.affiliateReferralService = affiliateReferralService;
	}
	
	@GetMapping("/affiliate/{affiliateId}")
	public ResponseEntity<AffiliateReferralModel> getAffiliateReferrals(@PathVariable String affiliateId){
		AffiliateReferralModel response = affiliateReferralService.getAffiliateReferrals(affiliateId);
		return new ResponseEntity<AffiliateReferralModel>(response,HttpStatus.OK);
	}

}
