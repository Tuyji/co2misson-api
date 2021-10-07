package com.co2nsensus.co2mission.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.co2nsensus.co2mission.model.request.AnalyticsRequest;
import com.co2nsensus.co2mission.model.response.analytics.AnalyticsModel;
import com.co2nsensus.co2mission.model.enums.AnalyticsBreakdownType;
import com.co2nsensus.co2mission.model.enums.AnalyticsDateInterval;
import com.co2nsensus.co2mission.service.AnalyticsService;

@RestController
@RequestMapping(value = "/analytics")
public class AnalyticsController {

	private final AnalyticsService analyticsService;

	public AnalyticsController(AnalyticsService analyticsService) {
		this.analyticsService = analyticsService;
	}

	@GetMapping("/{affiliateId}")
	public ResponseEntity<List<AnalyticsModel>> getAffiliateAnalytics(@PathVariable String affiliateId,
			@RequestParam(required = false) AnalyticsBreakdownType breakdown,
			@RequestParam(required = false) AnalyticsDateInterval dateInterval,
			@RequestParam(required = false) String customIntervalFrom,
			@RequestParam(required = false) String customIntervalTo) {
		return new ResponseEntity<>(analyticsService.getAnalytics(AnalyticsRequest.builder().affiliateId(affiliateId)
				.breakDown(breakdown).customIntervalFrom(customIntervalFrom).customIntervalTo(customIntervalTo)
				.interval(dateInterval).build()), HttpStatus.OK);
	}
}
