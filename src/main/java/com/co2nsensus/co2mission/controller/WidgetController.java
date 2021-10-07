package com.co2nsensus.co2mission.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.co2nsensus.co2mission.model.response.widget.AffiliateWidgetModel;
import com.co2nsensus.co2mission.service.WidgetService;

@RestController
@RequestMapping("/affiliates/{affiliateId}/widget")
public class WidgetController {

	private final WidgetService widgetService;
	
	public WidgetController(WidgetService widgetService) {
		this.widgetService = widgetService;
	}
	
	@GetMapping
	public ResponseEntity<?> getAffiliateWidgets(@PathVariable String affiliateId) {
		List<AffiliateWidgetModel> response = widgetService.getAffiliateWidgets(affiliateId);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<?> createWidget(@PathVariable String affiliateId,@RequestBody AffiliateWidgetModel widgetModel) {
		AffiliateWidgetModel response = widgetService.createWidget(affiliateId, widgetModel);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
}
