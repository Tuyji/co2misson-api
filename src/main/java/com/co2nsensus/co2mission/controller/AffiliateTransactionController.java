package com.co2nsensus.co2mission.controller;

import java.time.LocalDate;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.co2nsensus.co2mission.logging.Loggable;
import com.co2nsensus.co2mission.model.response.transaction.PaymentTransactionModelList;
import com.co2nsensus.co2mission.model.response.transaction.TransactionHistoryResponseModel;
import com.co2nsensus.co2mission.service.AffiliateTransactionService;

@RestController
@RequestMapping(value = "/transactions")
@Loggable
public class AffiliateTransactionController {

	private final AffiliateTransactionService affiliateTransactionService;

	public AffiliateTransactionController(AffiliateTransactionService affiliateTransactionService) {
		this.affiliateTransactionService = affiliateTransactionService;
	}

	@GetMapping("/{affiliateId}")
	public ResponseEntity<?> getLastNTransactions(
			@PageableDefault(page = 0, size = 5) @SortDefault.SortDefaults({
					@SortDefault(sort = "createdAt", direction = Sort.Direction.DESC) }) Pageable pageable,
			@PathVariable String affiliateId) {
		PaymentTransactionModelList paymentTransactionModels = affiliateTransactionService
				.getLastNTransactions(affiliateId, pageable);

		return new ResponseEntity<>(paymentTransactionModels, HttpStatus.OK);
	}

	@GetMapping("/{affiliateId}/mark")
	public ResponseEntity<?> markTransactionsRead(@PathVariable String affiliateId) {

		return new ResponseEntity<>(null, HttpStatus.OK);
	}

	@GetMapping("/history/{affiliateId}")
	public ResponseEntity<?> getTransactionHistory(@PathVariable String affiliateId,
			@RequestParam(required = false) String source,
			@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
			@RequestParam int page) {

		TransactionHistoryResponseModel transactionHistory = affiliateTransactionService
				.getTransactionHistory(affiliateId, source, startDate, endDate, page);
		return new ResponseEntity<>(transactionHistory, HttpStatus.OK);

	}

}
