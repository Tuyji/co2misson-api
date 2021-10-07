package com.co2nsensus.co2mission.exception;

public class MyFileNotFoundException extends AffiliateCustomNotFoundException {

    public MyFileNotFoundException(String code, String message) {
        super(code, message);
    }

    public MyFileNotFoundException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }
}