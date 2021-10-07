package com.co2nsensus.co2mission.service.co2nsenus;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.co2nsensus.co2mission.exception.AffiliateCreationException;
import com.co2nsensus.co2mission.exception.AffiliateErrorCodes;
import com.co2nsensus.co2mission.service.co2nsenus.exception.ChannelCreationException;
import com.co2nsensus.co2mission.service.co2nsenus.types.AccessTokenRequest;
import com.co2nsensus.co2mission.service.co2nsenus.types.AccessTokenResponse;
import com.co2nsensus.co2mission.service.co2nsenus.types.CreateChannelRequest;
import com.co2nsensus.co2mission.service.co2nsenus.types.CreateChannelResponse;
import com.co2nsensus.co2mission.service.co2nsenus.types.CreateWidgetRequest;
import com.co2nsensus.co2mission.service.co2nsenus.types.CreateWidgetResponse;
import com.co2nsensus.co2mission.service.co2nsenus.types.RefreshTokenRequest;

@Service
public class Co2nsensusInterfaceImpl implements Co2nsensusInterface {

	@Value("${app.co2nsensus.username}")
	private String username;
	@Value("${app.co2nsensus.password}")
	private String password;
	@Value("${app.co2nsensus.channelEndpoint}")
	private String channelEndpoint;
	@Value("${app.co2nsensus.widgetEndpoint}")
	private String widgetEndpoint;
	@Value("${app.co2nsensus.accessTokenEndpoint}")
	private String accessTokenEndpoint;
	@Value("${app.co2nsensus.refreshTokenEndpoint}")
	private String refreshTokenEndpoint;

	private String accessToken;
	private String refreshToken;

	private HttpComponentsClientHttpRequestFactory requestFactory;

	@PostConstruct
	public void constructReuestFactory() {
		CloseableHttpClient httpClient = HttpClients.custom().setSSLHostnameVerifier(new NoopHostnameVerifier())
				.build();
		requestFactory = new HttpComponentsClientHttpRequestFactory();
		requestFactory.setHttpClient(httpClient);
	}

	@Override
	public CreateChannelResponse createChannel(CreateChannelRequest createChannelRequest) {
		if (StringUtils.isAllBlank(accessToken))
			getAuthToken();

		ResponseEntity<CreateChannelResponse> response = sendChannelRequest(createChannelRequest);
		if (response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
			refreshToken();
			response = sendChannelRequest(createChannelRequest);
			if (response.getStatusCode() != HttpStatus.CREATED) {
				throw new AffiliateCreationException(AffiliateErrorCodes.AFFILIATE_CREATION_ERROR.getCode(),
						AffiliateErrorCodes.AFFILIATE_CREATION_ERROR.getMessage());
			}
		}
		if (response.getStatusCode() == HttpStatus.CREATED) {
			return response.getBody();
		} else {
			throw new AffiliateCreationException(AffiliateErrorCodes.AFFILIATE_CREATION_ERROR.getCode(),
					AffiliateErrorCodes.AFFILIATE_CREATION_ERROR.getMessage());
		}
	}

	private ResponseEntity<CreateChannelResponse> sendChannelRequest(CreateChannelRequest createChannelRequest) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("AUTHORIZATION", "Bearer " + accessToken);
		HttpEntity<CreateChannelRequest> request = new HttpEntity<CreateChannelRequest>(createChannelRequest, headers);

		return new RestTemplate(requestFactory).exchange(channelEndpoint, HttpMethod.POST, request,
				CreateChannelResponse.class);
	}

	@Override
	public CreateWidgetResponse createWidget(CreateWidgetRequest createWidgetRequest) throws ChannelCreationException {
		if (StringUtils.isAllBlank(accessToken))
			getAuthToken();

		createWidgetRequest.setChannel("/api/channels/" + createWidgetRequest.getChannel());
		ResponseEntity<CreateWidgetResponse> response = null;
		try {
			response = sendCreateWidgetRequest(createWidgetRequest);
		} catch (HttpClientErrorException e) {
			if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
				refreshToken();
				response = sendCreateWidgetRequest(createWidgetRequest);
			} else {
				throw new AffiliateCreationException(AffiliateErrorCodes.AFFILIATE_CREATION_ERROR.getCode(),
						AffiliateErrorCodes.AFFILIATE_CREATION_ERROR.getMessage());
			}
		}
		return response.getBody();
	}

	private ResponseEntity<CreateWidgetResponse> sendCreateWidgetRequest(CreateWidgetRequest createWidgetRequest) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("AUTHORIZATION", "Bearer " + accessToken);
		HttpEntity<CreateWidgetRequest> request = new HttpEntity<CreateWidgetRequest>(createWidgetRequest, headers);

		return new RestTemplate(requestFactory).exchange(widgetEndpoint, HttpMethod.POST, request,
				CreateWidgetResponse.class);
	}

	private void getAuthToken() {
		AccessTokenRequest requestBody = new AccessTokenRequest();
		requestBody.setUsername(username);
		requestBody.setPassword(password);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<AccessTokenRequest> request = new HttpEntity<AccessTokenRequest>(requestBody, headers);
		ResponseEntity<AccessTokenResponse> response = new RestTemplate(requestFactory).exchange(accessTokenEndpoint,
				HttpMethod.POST, request, AccessTokenResponse.class);
		accessToken = response.getBody().getToken();
		refreshToken = response.getBody().getRefresh_token();
	}

	private void refreshToken() {
		RefreshTokenRequest requestBody = new RefreshTokenRequest();
		requestBody.setRefresh_token(refreshToken);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<RefreshTokenRequest> request = new HttpEntity<RefreshTokenRequest>(requestBody, headers);
		ResponseEntity<AccessTokenResponse> response = new RestTemplate(requestFactory).exchange(refreshTokenEndpoint,
				HttpMethod.POST, request, AccessTokenResponse.class);
		if (response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
			getAuthToken();
			return;
		} else if (response.getStatusCode() == HttpStatus.OK) {
			accessToken = response.getBody().getToken();
			refreshToken = response.getBody().getRefresh_token();
		}

	}

}
