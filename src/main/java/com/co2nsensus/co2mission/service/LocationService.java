package com.co2nsensus.co2mission.service;

import java.util.List;

import com.co2nsensus.co2mission.model.response.location.CityModel;
import com.co2nsensus.co2mission.model.response.location.CountryModel;

public interface LocationService {
	List<CountryModel> getCountries();
	
	List<CityModel> getCountryCities(Long countryId);
}
