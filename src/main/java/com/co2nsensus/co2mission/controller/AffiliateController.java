package com.co2nsensus.co2mission.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.co2nsensus.co2mission.logging.Loggable;
import com.co2nsensus.co2mission.model.response.affiliate.AffiliateModel;
import com.co2nsensus.co2mission.model.response.affiliate.AffiliateModelList;
import com.co2nsensus.co2mission.model.response.subscription.AffiliateSubscriptionsResponseModel;
import com.co2nsensus.co2mission.model.response.verification.PaymentVerificationModel;
import com.co2nsensus.co2mission.service.AffiliateService;
import com.co2nsensus.co2mission.service.AffiliateSubscriptionService;
import com.co2nsensus.co2mission.service.PaymentVerificationService;

@RestController
@RequestMapping(value = "/affiliates")
@Loggable
public class AffiliateController {

	private final AffiliateService affiliateService;
	private final AffiliateSubscriptionService affiliateSubscriptionService;
	private final PaymentVerificationService paymentVerificationService;

	public AffiliateController(AffiliateService affiliateService, PaymentVerificationService paymentVerificationService,
			AffiliateSubscriptionService affiliateSubscriptionService) {
		this.affiliateService = affiliateService;
		this.paymentVerificationService = paymentVerificationService;
		this.affiliateSubscriptionService = affiliateSubscriptionService;
	}

	@GetMapping
	public ResponseEntity<?> getAffiliates() {
		AffiliateModelList affiliates = affiliateService.getAffiliates();
		return new ResponseEntity<>(affiliates, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getAffiliateById(@PathVariable String id) {
		AffiliateModel affiliateModel = affiliateService.getAffiliateById(id);
		return new ResponseEntity<>(affiliateModel, HttpStatus.OK);
	}

	@GetMapping("/")
	public ResponseEntity<?> getAffiliate() {
		AffiliateModel affiliateModel = affiliateService.getAffiliate();
		return new ResponseEntity<>(affiliateModel, HttpStatus.OK);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateAffiliateInformation(@PathVariable String id,
			@RequestBody AffiliateModel affiliateModel) {
		affiliateModel = affiliateService.updateAffiliate(id,affiliateModel);
		return new ResponseEntity<>(affiliateModel, HttpStatus.OK);
	}

	@GetMapping("/{id}/verify-payment")
	public ResponseEntity<?> verifyPayment(@PathVariable String id, @RequestParam String authorizationCode) {
		PaymentVerificationModel response = paymentVerificationService.verifyPayment(id, authorizationCode);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/{id}/subscribers")
	public ResponseEntity<?> getAffiliateSubscribers(@PathVariable String id, @RequestParam String source) {
		AffiliateSubscriptionsResponseModel responseModel = affiliateSubscriptionService.getAffiliateSubscribers(id,
				source);
		return new ResponseEntity<>(responseModel, HttpStatus.OK);
	}

}
