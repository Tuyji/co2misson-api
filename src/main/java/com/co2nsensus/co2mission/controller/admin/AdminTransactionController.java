package com.co2nsensus.co2mission.controller.admin;

import java.io.IOException;
import java.time.LocalDate;

import org.springframework.data.domain.Sort.Direction;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.co2nsensus.co2mission.model.dto.PaymentTransactionModel;
import com.co2nsensus.co2mission.model.entity.PaymentTransaction;
import com.co2nsensus.co2mission.model.enums.AffiliateVerificationStatus;
import com.co2nsensus.co2mission.model.enums.PaymentVerificationStatus;
import com.co2nsensus.co2mission.model.enums.SourceType;
import com.co2nsensus.co2mission.model.response.transaction.AffiliateTransactionMetrics;
import com.co2nsensus.co2mission.model.response.transaction.AffiliateTransactionMetricsList;
import com.co2nsensus.co2mission.service.AffiliateTransactionService;
import com.co2nsensus.co2mission.service.PaymentTransactionService;
import com.co2nsensus.co2mission.service.RedisService;

@RestController
@RequestMapping("/admin/transaction")
public class AdminTransactionController {

	private final PaymentTransactionService paymentTransactionService;
	private final RedisService redisService;
	private final AffiliateTransactionService affiliateTransactionService;

	public AdminTransactionController(PaymentTransactionService paymentTransactionService, RedisService redisService,
			AffiliateTransactionService affiliateTransactionService) {
		this.paymentTransactionService = paymentTransactionService;
		this.redisService = redisService;
		this.affiliateTransactionService = affiliateTransactionService;
	}

	@PostMapping
	public ResponseEntity<?> createPaymentTransaction(@RequestBody PaymentTransactionModel paymentTransactionModel) {

		PaymentTransaction paymentTransaction = paymentTransactionService
				.createPaymentTransaction(paymentTransactionModel);
		try {
			redisService.createOrUpdateTotalSales(paymentTransaction);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
	}

	@GetMapping("/history/{affiliateId}")
	public ResponseEntity<?> getTransactionHistory(@PathVariable String affiliateId,
			@RequestParam(required = false) SourceType sourceType,
			@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
			@RequestParam int page, @RequestParam(required = false) String projectId,
			@RequestParam(value = "sort") String sort, @RequestParam(value = "direction") Direction direction,
			@RequestParam(value = "size") int size, @RequestParam(value = "filter", required = false) String filter,
			@RequestParam(required = false, value = "verificationStatus") AffiliateVerificationStatus verificationStatus,
			@RequestParam(required = false, value = "paymentVerificationStatus") PaymentVerificationStatus paymentVerificationStatus,
			@RequestParam(required = false, value = "platform") String platformName) {

		AffiliateTransactionMetrics transactionHistory = affiliateTransactionService.getTransactionMetrics(affiliateId,
				sourceType, startDate, endDate, page, projectId, sort, size, filter, direction, verificationStatus,
				paymentVerificationStatus, platformName);
		return new ResponseEntity<>(transactionHistory, HttpStatus.OK);

	}

	@GetMapping("/history")
	public ResponseEntity<?> getAffilatesWithTransactionHistory(@RequestParam(required = false) SourceType sourceType,
			@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
			@RequestParam int page, @RequestParam(required = false) String projectId,
			@RequestParam(value = "sort") String sort, @RequestParam(value = "direction") Direction direction,
			@RequestParam(value = "size") int size, @RequestParam(value = "filter", required = false) String filter,
			@RequestParam(required = false, value = "verificationStatus") AffiliateVerificationStatus verificationStatus,
			@RequestParam(required = false, value = "paymentVerificationStatus") PaymentVerificationStatus paymentVerificationStatus,
			@RequestParam(required = false, value = "platform") String platformName) {

		AffiliateTransactionMetricsList response = affiliateTransactionService.getAllTransactionMetrics(sourceType,
				startDate, endDate, page, projectId, sort, size, filter, direction, verificationStatus,
				paymentVerificationStatus, platformName);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}
}
