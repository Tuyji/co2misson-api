package com.co2nsensus.co2mission.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.co2nsensus.co2mission.model.response.location.CityModel;
import com.co2nsensus.co2mission.model.response.location.CountryModel;
import com.co2nsensus.co2mission.service.LocationService;
import com.co2nsensus.co2mission.service.RedisService;

@Service
public class LocationServiceImpl implements LocationService {

	private final RedisService redisService;

	public LocationServiceImpl(RedisService redisService) {
		this.redisService = redisService;
	}

	@Override
	public List<CountryModel> getCountries() {
		return redisService.getCountries();
	}

	@Override
	public List<CityModel> getCountryCities(Long countryId) {
		return redisService.getCityListByCountry(countryId);
	}

}
