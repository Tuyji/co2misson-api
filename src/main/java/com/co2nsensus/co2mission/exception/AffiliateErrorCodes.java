package com.co2nsensus.co2mission.exception;

public enum AffiliateErrorCodes {

	RUN_TIME_EXCEPTION("000", "Runtime Exception"), AFFILIATE_NOT_FOUND("100", "Affiliate not found."),
	AFFILIATE_REFERRAL_NOT_FOUND("101", "Affiliate referral not found."),
	EXCHANGE_RATE_NOT_FOUND("102", "Exchange rate not found."), FILE_NOT_FOUND("103", "File not found."),
	PLATFORM_NOT_FOUND("104", "Platform not found."), SUBSCRIPTION_NOT_FOUND("105", "Subscription not found."),
	AFFILIATE_CREATION_ERROR("200", "Affiliate not found."), ANALYTICS_EXCEPTION("201", "Affiliate not found."),
	FILE_STORAGE_EXCEPTION("202", "Affiliate not found."),
	REDIS_DESERIALIZATION_EXCEPTION("203", "Affiliate not found."),
	RESET_PASSWORD_TOKEN_INVALID("204", "Affiliate not found."),
	VERIFICATION_FILE_NOT_SAVED("205", "Affiliate not found.");

	private final String code;
	private final String message;

	private AffiliateErrorCodes(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public String getCode() {
		return code;
	}

	@Override
	public String toString() {
		return code + ": " + message;
	}
}
