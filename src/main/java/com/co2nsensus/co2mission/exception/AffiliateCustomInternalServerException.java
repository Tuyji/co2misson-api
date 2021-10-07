package com.co2nsensus.co2mission.exception;

public class AffiliateCustomInternalServerException extends AffiliateRuntimeException {


    public AffiliateCustomInternalServerException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public AffiliateCustomInternalServerException(String code, String message) {
        super(code, message);
    }

    public AffiliateCustomInternalServerException(String message) {
        super(message);
    }
}
