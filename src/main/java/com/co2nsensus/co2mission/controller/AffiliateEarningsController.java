package com.co2nsensus.co2mission.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.co2nsensus.co2mission.logging.Loggable;
import com.co2nsensus.co2mission.model.response.earnings.EarningModel;
import com.co2nsensus.co2mission.model.response.earnings.MonthlyEarningsModel;
import com.co2nsensus.co2mission.service.AffiliateEarningsService;

@RestController
@RequestMapping(value = "/earnings")
@Loggable
public class AffiliateEarningsController {

	private final AffiliateEarningsService affiliateEarningsService;

	public AffiliateEarningsController(AffiliateEarningsService affiliateEarningsService) {
		this.affiliateEarningsService = affiliateEarningsService;
	}

	@GetMapping("/{affiliateId}/{year}")
	public List<MonthlyEarningsModel> getMonthlyEarningsByYear(@PathVariable String affiliateId,
			@PathVariable String year) {
		return affiliateEarningsService.getMonthlyEarningsByYear(affiliateId, year);
	}

	@GetMapping("/{affiliateId}/{year}/{month}")
	public MonthlyEarningsModel getMonthlyEarnings(@PathVariable String affiliateId, @PathVariable String year,
			@PathVariable int month) {
		return affiliateEarningsService.getMonthlyEarnings(affiliateId, year, month);
	}

	@GetMapping("/{affiliateId}/current/year")
	public List<MonthlyEarningsModel> getMonthlyEarningsCurrentByYear(@PathVariable String affiliateId) {
		return affiliateEarningsService.getMonthlyEarningsByYear(affiliateId,
				String.valueOf(LocalDate.now().getYear()));
	}

	@GetMapping("/{affiliateId}/current")
	public EarningModel getCurrentEarnings(@PathVariable String affiliateId) {
		EarningModel response = affiliateEarningsService.getAffiliateEarningSummary(affiliateId);

//		
//		MonthlyEarningsModel currentEarnings = affiliateEarningsService.getMonthlyEarnings(affiliateId,
//				String.valueOf(LocalDate.now().getYear()), LocalDate.now().getMonthValue());
//		currentEarnings.setMonth(LocalDateTime.now().getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH));
//		currentEarnings.setYear(LocalDateTime.now().getYear());
		return response;
	}

	@GetMapping("/{affiliateId}/period")
	public EarningModel getPeriodEarnings(@PathVariable String affiliateId,
			@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
		EarningModel response = affiliateEarningsService.getPeriodEarnings(affiliateId, startDate, endDate);
		return response;
	}

}
