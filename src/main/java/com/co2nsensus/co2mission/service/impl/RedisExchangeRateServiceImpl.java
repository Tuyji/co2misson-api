package com.co2nsensus.co2mission.service.impl;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import com.co2nsensus.co2mission.exception.AffiliateErrorCodes;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.co2nsensus.co2mission.exception.ExchangeRateNotFoundException;
import com.co2nsensus.co2mission.repo.ExchangeRateRepository;
import com.co2nsensus.co2mission.repo.RedisRepository;
import com.co2nsensus.co2mission.service.RedisExchangeRateService;

@Component
public class RedisExchangeRateServiceImpl implements RedisExchangeRateService {

	private final ExchangeRateRepository exchangeRateRepository;
	private final RedisRepository redisRepository;
	private final RedisTemplate<String, Object> redisTemplate;
	private static final String DELIMETER = "#";

	public RedisExchangeRateServiceImpl(ExchangeRateRepository exchangeRateRepository, RedisRepository redisRepository,
			@Qualifier("redisTemplate") RedisTemplate<String, Object> redisTemplate) {
		this.exchangeRateRepository = exchangeRateRepository;
		this.redisRepository = redisRepository;
		this.redisTemplate = redisTemplate;
	}

	@Override
	public BigDecimal getExchangeRate(String from, String to) throws ExchangeRateNotFoundException {
		BigDecimal rate = null;
		Object rateObject = redisRepository.getDataFromRedis("CURRENCY", from + DELIMETER + to);
		if (Objects.isNull(rateObject)) {
			rate = exchangeRateRepository.findByFromCurrencyAndToCurrency(from, to)
					.orElseThrow(() -> new ExchangeRateNotFoundException(AffiliateErrorCodes.EXCHANGE_RATE_NOT_FOUND.getCode(),
							AffiliateErrorCodes.EXCHANGE_RATE_NOT_FOUND.getMessage() + from + DELIMETER + to)).getRate();
			redisRepository.putDataToRedis("CURRENCY", from + DELIMETER + to, rate);
			redisTemplate.expire("CURRENCY", 6, TimeUnit.HOURS);
			return rate;
		} else {
			return new BigDecimal(String.valueOf(rateObject));
		}
	}
}
