package com.co2nsensus.co2mission.service.payment.paypal;

import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.co2nsensus.co2mission.model.enums.PaymentVerificationStatus;
import com.co2nsensus.co2mission.model.response.affiliate.AffiliateModel;
import com.co2nsensus.co2mission.model.response.verification.PaymentVerificationModel;
import com.co2nsensus.co2mission.service.AffiliateService;
import com.co2nsensus.co2mission.service.payment.paypal.types.response.PaypalAuthorizationResponse;
import com.co2nsensus.co2mission.service.payment.paypal.types.response.PaypalIdentityResponse;

@Component
public class PaypalInterfaceImpl implements PaypalInterface {

	private final AffiliateService affiliateService;
	private final RestTemplate restTemplate;

	private String accessToken;
	private String refreshToken;

	@Value("${app.paypal.token.endpoint}")
	private String tokenEndpoint;

	@Value("${app.paypal.identity.endpoint}")
	private String identityEndpoint;

	@Value("${app.paypal.client.id}")
	private String clientId;

	@Value("${app.paypal.client.secret}")
	private String clientSecret;

	public PaypalInterfaceImpl(AffiliateService affiliateService, RestTemplate restTemplate) {
		this.affiliateService = affiliateService;
		this.restTemplate = restTemplate;
	}

	@Override
	public PaymentVerificationModel verifyUserPaymentInfo(String affiliateId, String authorizationCode) {
		PaypalAuthorizationResponse authResponse = getAccessToken(authorizationCode);
		PaypalIdentityResponse identityResponse = getUserIdentity(authResponse.getAccess_token());
		AffiliateModel affiliate = affiliateService.getAffiliateById(affiliateId);
		boolean userMatched = (affiliate.getFirstName() + " " + affiliate.getLastName())
				.equals(identityResponse.getName())
				&& identityResponse.getEmails().stream().anyMatch(e -> e.getValue().equals(affiliate.getEmail()));
		return userMatched ? new PaymentVerificationModel(PaymentVerificationStatus.VERIFIED)
				: new PaymentVerificationModel(PaymentVerificationStatus.INFO_NOT_MATCHED);
	}

	public PaypalAuthorizationResponse getAccessToken(String authorizationCode) {
		HttpHeaders headers = new HttpHeaders();
		String authString = clientId + ":" + clientSecret;
		headers.setBasicAuth(new String(Base64.getEncoder().encode(authString.getBytes())));
		HttpEntity<HttpHeaders> request = new HttpEntity<>(headers);
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(tokenEndpoint)
				.queryParam("grant_type", "authorization_code").queryParam("code", authorizationCode);

		ResponseEntity<PaypalAuthorizationResponse> response = restTemplate.exchange(builder.build(false).toUri(),
				HttpMethod.POST, request, PaypalAuthorizationResponse.class);
		return response.getBody();
	}

	public PaypalIdentityResponse getUserIdentity(String accessToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + accessToken);
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<HttpHeaders> request = new HttpEntity<>(headers);
		ResponseEntity<PaypalIdentityResponse> response = restTemplate.exchange(identityEndpoint, HttpMethod.GET,
				request, PaypalIdentityResponse.class);
		return response.getBody();
	}
}
