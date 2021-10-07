package com.co2nsensus.co2mission.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.co2nsensus.co2mission.model.response.location.CityModel;
import com.co2nsensus.co2mission.model.response.location.CountryModel;
import com.co2nsensus.co2mission.service.LocationService;

@RestController
@RequestMapping("/location")
public class LocationController {

	private final LocationService locationService;

	public LocationController(LocationService locationService) {
		this.locationService = locationService;
	}

	@GetMapping("/countries")
	private ResponseEntity<List<CountryModel>> getCountries() {
		return new ResponseEntity<List<CountryModel>>(locationService.getCountries(), HttpStatus.OK);

	}

	@GetMapping("/countries/{countryId}/cities")
	private ResponseEntity<List<CityModel>> getCountryCities(@PathVariable Long countryId) {
		return new ResponseEntity<List<CityModel>>(locationService.getCountryCities(countryId), HttpStatus.OK);
	}
}
