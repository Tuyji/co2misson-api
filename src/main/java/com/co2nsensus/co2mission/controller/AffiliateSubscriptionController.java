package com.co2nsensus.co2mission.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.co2nsensus.co2mission.model.response.subscription.AffiliateSubscriptionsResponseModel;
import com.co2nsensus.co2mission.service.AffiliateSubscriptionService;

@RestController
@RequestMapping("/affiliates/{affiliateId}/subscription")
public class AffiliateSubscriptionController {
	private final AffiliateSubscriptionService affiliateSubscriptionService;

	public AffiliateSubscriptionController(AffiliateSubscriptionService affiliateSubscriptionService) {
		this.affiliateSubscriptionService = affiliateSubscriptionService;
	}

	@GetMapping("")
	public ResponseEntity<?> getAffiliateSubscriptions(@PathVariable String affiliateId,
			@RequestParam(required = false) String source) {
		AffiliateSubscriptionsResponseModel response = affiliateSubscriptionService.getAffiliateSubscribers(affiliateId,
				source);
		return new ResponseEntity<AffiliateSubscriptionsResponseModel>(response, HttpStatus.OK);
	}
}
