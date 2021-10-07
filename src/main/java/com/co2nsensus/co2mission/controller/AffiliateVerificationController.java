package com.co2nsensus.co2mission.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.co2nsensus.co2mission.model.enums.VerificationFileType;
import com.co2nsensus.co2mission.model.response.VerificationFileModel;
import com.co2nsensus.co2mission.model.response.VerificationFileModelList;
import com.co2nsensus.co2mission.model.response.verification.PaymentVerificationModel;
import com.co2nsensus.co2mission.service.AffiliateVerificationService;
import com.co2nsensus.co2mission.service.PaymentVerificationService;

@RestController
@RequestMapping("/affiliates/{affiliateId}/verification")
public class AffiliateVerificationController {

	private final AffiliateVerificationService affiliateVerificationService;
	private final PaymentVerificationService paymentVerificationService;

	public AffiliateVerificationController(AffiliateVerificationService affiliateVerificationService,
			PaymentVerificationService paymentVerificationService) {
		this.affiliateVerificationService = affiliateVerificationService;
		this.paymentVerificationService = paymentVerificationService;
	}

	@GetMapping("/files")
	public ResponseEntity<?> getVerificationFiles(@PathVariable String affiliateId) {
		VerificationFileModelList response = affiliateVerificationService.getVerificationFiles(affiliateId);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}


	@PostMapping("/files")
	public ResponseEntity<VerificationFileModel> uploadVerificationFile(@PathVariable String affiliateId,
			@RequestParam VerificationFileType fileType, MultipartFile file) {
		VerificationFileModel response = affiliateVerificationService.uploadVerificationFile(affiliateId, fileType,
				file);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/payment")
	public ResponseEntity<PaymentVerificationModel> verifyPayment(@PathVariable String affiliateId,
			@RequestParam String authorizationCode) {
		PaymentVerificationModel response = paymentVerificationService.verifyPayment(affiliateId, authorizationCode);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
