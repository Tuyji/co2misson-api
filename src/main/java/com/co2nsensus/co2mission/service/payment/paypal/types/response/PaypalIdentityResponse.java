package com.co2nsensus.co2mission.service.payment.paypal.types.response;

import java.util.List;

import lombok.Data;

@Data
public class PaypalIdentityResponse {
	public String user_id;
	public String name;
	public String given_name;
	public String family_name;
	public String payer_id;
	public PaypalIdentityAddress address;
	public String verified_account;
	public List<PaypalIdentityEmail> emails;
}
