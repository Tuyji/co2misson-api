package com.co2nsensus.co2mission.service;

import java.io.IOException;
import java.util.List;

import com.co2nsensus.co2mission.model.dto.MonthlyChannelData;
import com.co2nsensus.co2mission.model.entity.PaymentTransaction;
import com.co2nsensus.co2mission.model.response.location.CityModel;
import com.co2nsensus.co2mission.model.response.location.CountryModel;

public interface RedisService {
	public List<MonthlyChannelData> getMonthlySales(String affiliateId, String year);

	public MonthlyChannelData getMonthlySales(String affiliateId, String year, int month);

	public void createOrUpdateTotalSales(PaymentTransaction paymentTransaction) throws IOException;

	public int getUnseenTransactionCount(String affiliateId);

	public void incrementUnseenTransactionCount(String affiliateId);

	public void resetUnseenTransactionCount(String affiliateId);
	
	List<CountryModel> getCountries();
	
	List<CityModel> getCityListByCountry(Long countryId);
}
