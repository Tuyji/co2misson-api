package com.co2nsensus.co2mission.exception;

public class SubscriptionNotFoundException extends AffiliateCustomNotFoundException {

    /**
	 * 
	 */
	private static final long serialVersionUID = -2922358378216460437L;

	public SubscriptionNotFoundException(String code, String message) {
        super(code, message);
    }
}
