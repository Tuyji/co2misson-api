package com.co2nsensus.co2mission.exception;

public class ExchangeRateNotFoundException extends AffiliateCustomNotFoundException {

    public ExchangeRateNotFoundException(String code, String message) {
        super(message);
    }

    public ExchangeRateNotFoundException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }

}