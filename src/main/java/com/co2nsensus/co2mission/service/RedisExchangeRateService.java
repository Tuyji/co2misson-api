package com.co2nsensus.co2mission.service;

import java.math.BigDecimal;

public interface RedisExchangeRateService {
	public BigDecimal getExchangeRate(String from,String to);
}
