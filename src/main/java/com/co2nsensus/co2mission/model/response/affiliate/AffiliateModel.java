package com.co2nsensus.co2mission.model.response.affiliate;

import java.util.List;

import com.co2nsensus.co2mission.model.enums.AffiliateVerificationStatus;
import com.co2nsensus.co2mission.model.enums.PaymentVerificationStatus;
import com.co2nsensus.co2mission.model.response.platform.AffiliatePlatformModel;

import lombok.Data;

@Data
public class AffiliateModel {
	private String id;
	private String channelId;
	private String firstName;
	private String lastName;
	private String email;
	private String phone;
	private String companyName;
	private String postcode;
	private String state;
	private String country;
	private String city;
	private AffiliateVerificationStatus verificationStatus;
	private PaymentVerificationStatus paymentVerificationStatus;
	private List<AffiliatePlatformModel> platforms;
	private int newTransactionCount;
}
