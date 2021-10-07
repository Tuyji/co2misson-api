package com.co2nsensus.co2mission.service.payment.paypal.types.response;

import lombok.Data;

@Data
public class PaypalIdentityAddress {
	public String street_address;
    public String locality;
    public String region;
    public String postal_code;
    public String country;
}
