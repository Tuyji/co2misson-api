package com.co2nsensus.co2mission.exception;

public class AffiliateRuntimeException extends RuntimeException {

    private String code;

    private static final long serialVersionUID = -5541792733035452303L;

    public AffiliateRuntimeException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public AffiliateRuntimeException(String code, String message) {
        super(message);
        this.code = code;
    }

    public AffiliateRuntimeException(String message) {
        super(message);
    }

    public String getCode() {
        return code;
    }
}
