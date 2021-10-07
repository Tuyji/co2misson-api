package com.co2nsensus.co2mission.exception;

public class RedisDeserializationException extends AffiliateCustomInternalServerException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7410698845518656101L;

	public RedisDeserializationException(String code, String message) {
        super(code, message);
    }

    public RedisDeserializationException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }

}
