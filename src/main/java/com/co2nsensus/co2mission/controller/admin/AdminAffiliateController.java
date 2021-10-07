package com.co2nsensus.co2mission.controller.admin;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.co2nsensus.co2mission.model.enums.AffiliateVerificationStatus;
import com.co2nsensus.co2mission.model.response.affiliate.AffiliateModel;
import com.co2nsensus.co2mission.model.response.affiliate.AffiliateResponse;
import com.co2nsensus.co2mission.service.AffiliateService;

@RestController
@RequestMapping("/admin/affiliates")
public class AdminAffiliateController {

	private final AffiliateService affiliateService;

	public AdminAffiliateController(AffiliateService affiliateService) {
		this.affiliateService = affiliateService;
	}

	@GetMapping("")
	public ResponseEntity<?> getAffiliatesPaged(@RequestParam(value = "sort") String sort,
			@RequestParam(value = "page") int page, @RequestParam(value = "size") int size,
			@RequestParam(value = "filter", required = false) String filter) {

		Pageable requestedPage = PageRequest.of(page, size, Sort.by(sort));
		AffiliateResponse affiliateResponse = affiliateService.getAffiliatesPaged(filter, requestedPage);
		return new ResponseEntity<>(affiliateResponse, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getAffiliate(@PathVariable String id) {
		AffiliateModel affiliateResponse = affiliateService.getAffiliateById(id);
		return new ResponseEntity<>(affiliateResponse, HttpStatus.OK);
	}

	@PostMapping("/{id}/status")
	public ResponseEntity<?> updateAffiliateStatus(@PathVariable String id,
			@RequestParam AffiliateVerificationStatus affiliateVerificationStatus) {
		affiliateService.updateAffiliateStatus(id, affiliateVerificationStatus);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
