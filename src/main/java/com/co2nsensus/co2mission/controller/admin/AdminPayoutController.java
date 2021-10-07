package com.co2nsensus.co2mission.controller.admin;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.co2nsensus.co2mission.model.response.earnings.PayoutBatchModel;
import com.co2nsensus.co2mission.model.response.earnings.PayoutModel;
import com.co2nsensus.co2mission.service.PayoutService;

@RestController
@RequestMapping("/admin/payout")
public class AdminPayoutController {

	private final PayoutService payoutService;

	public AdminPayoutController(PayoutService payoutService) {
		this.payoutService = payoutService;
	}

	public ResponseEntity<List<PayoutModel>> getAffiliatePayouts(String affiliateId) {
		List<PayoutModel> response = payoutService.getAffiliatePayouts(affiliateId);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	public ResponseEntity<List<PayoutModel>> getBatchPayouts(String batchId) {
		List<PayoutModel> response = payoutService.getBatchPayouts(batchId);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	public ResponseEntity<List<PayoutBatchModel>> getBatches() {
		List<PayoutBatchModel> response = payoutService.getBatches();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
