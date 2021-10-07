package com.co2nsensus.co2mission.service.payment.paypal.types.response;

import lombok.Data;

@Data
public class PaypalAuthorizationResponse {
	private String token_type;
	private String expires_in;
	private String refresh_token;
	private String access_token;
	
}
