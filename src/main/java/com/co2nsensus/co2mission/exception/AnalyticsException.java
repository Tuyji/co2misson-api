package com.co2nsensus.co2mission.exception;

public class AnalyticsException extends AffiliateCustomInternalServerException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 8513774739294020360L;

	public AnalyticsException(String code, String message) {
        super(code, message);
    }
    
    public AnalyticsException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }

}
