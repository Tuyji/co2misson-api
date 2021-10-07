package com.co2nsensus.co2mission.exception;

public class AffiliateCustomNotFoundException extends AffiliateRuntimeException {

    public AffiliateCustomNotFoundException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public AffiliateCustomNotFoundException(String code, String message) {
        super(code, message);
    }

    public AffiliateCustomNotFoundException(String message) {
        super(message);
    }
}
