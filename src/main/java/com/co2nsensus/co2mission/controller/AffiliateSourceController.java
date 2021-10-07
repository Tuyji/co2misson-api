package com.co2nsensus.co2mission.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.co2nsensus.co2mission.model.enums.SourceType;
import com.co2nsensus.co2mission.model.response.source.AffiliateSourceModel;
import com.co2nsensus.co2mission.model.response.source.AffiliateSourceModelList;
import com.co2nsensus.co2mission.service.AffiliateSourceService;

@RestController
@RequestMapping("/affiliates/{affiliateId}/source")
public class AffiliateSourceController {

	private final AffiliateSourceService affiliateSourceService;

	public AffiliateSourceController(AffiliateSourceService affiliateSourceService) {
		this.affiliateSourceService = affiliateSourceService;
	}

	@GetMapping
	public ResponseEntity<AffiliateSourceModelList> getAffiliateSources(@PathVariable String affiliateId,
			@RequestParam SourceType sourceType) {
		AffiliateSourceModelList response = affiliateSourceService.getAffiliateSources(affiliateId, sourceType);
		return new ResponseEntity<AffiliateSourceModelList>(response, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<AffiliateSourceModel> addAffiliateSource(@PathVariable String affiliateId,
			@RequestBody AffiliateSourceModel source) {
		AffiliateSourceModel response = affiliateSourceService.addSource(affiliateId, source);
		
		return new ResponseEntity<AffiliateSourceModel>(response, HttpStatus.OK);
	}
}
