package com.co2nsensus.co2mission.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.co2nsensus.co2mission.logging.Loggable;
import com.co2nsensus.co2mission.model.request.AffiliateApplicationRequest;
import com.co2nsensus.co2mission.model.response.application.AffiliateApplicationModel;
import com.co2nsensus.co2mission.service.AffiliateApplicationService;


@RestController
@RequestMapping(value = "/application")
@Loggable
public class AffiliateApplicationController {

	private final AffiliateApplicationService affiliateApplicationService;

	public AffiliateApplicationController(AffiliateApplicationService affiliateApplicationService) {
		this.affiliateApplicationService = affiliateApplicationService;
	}

	@PostMapping
	public ResponseEntity<?> createApplication(@RequestBody @Valid AffiliateApplicationRequest affliateApplicationRequest) {
		AffiliateApplicationModel applicationModel = affiliateApplicationService.createApplication(affliateApplicationRequest);
		return new ResponseEntity<>(applicationModel, HttpStatus.CREATED);
	}

}
