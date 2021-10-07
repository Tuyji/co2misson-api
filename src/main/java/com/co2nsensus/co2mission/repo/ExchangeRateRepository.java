package com.co2nsensus.co2mission.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.co2nsensus.co2mission.model.entity.ExchangeRate;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {

	Optional<ExchangeRate> findByFromCurrencyAndToCurrency(String fromCurrency,String toCurrency);
}
