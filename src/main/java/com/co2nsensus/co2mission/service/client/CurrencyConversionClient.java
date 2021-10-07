package com.co2nsensus.co2mission.service.client;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.co2nsensus.co2mission.model.dto.AffiliateAmountType;
import com.co2nsensus.co2mission.service.RedisExchangeRateService;

@Component
@Lazy
public class CurrencyConversionClient {

	private final RedisExchangeRateService redisExchangeRateService;

	public CurrencyConversionClient(RedisExchangeRateService redisExchangeRateService) {
		this.redisExchangeRateService = redisExchangeRateService;
	}

	public AffiliateAmountType convertCurrency(AffiliateAmountType sourceAmountType) {
		if ("USD".equals(sourceAmountType.getCurrency()))
			return sourceAmountType;
		AffiliateAmountType targetAmountType = new AffiliateAmountType();
		targetAmountType.setAmount(redisExchangeRateService.getExchangeRate(sourceAmountType.getCurrency(), "USD")
				.multiply(sourceAmountType.getAmount()));
		targetAmountType.setCurrency("USD");
		return targetAmountType;
	}

	public AffiliateAmountType convertCurrency(AffiliateAmountType sourceAmountType, String targetCurrency) {
		AffiliateAmountType targetAmountType = new AffiliateAmountType();
		targetAmountType.setAmount(redisExchangeRateService.getExchangeRate(sourceAmountType.getCurrency(), "USD")
				.multiply(sourceAmountType.getAmount()).multiply(sourceAmountType.getAmount()));
		targetAmountType.setCurrency(targetCurrency);
		return targetAmountType;
	}

}
