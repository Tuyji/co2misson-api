package com.co2nsensus.co2mission.config;

import java.util.Collections;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.co2nsensus.co2mission.interceptor.RequestResponseLoggingInterceptor;

@Configuration
public class RestClientConfig {

	@Bean
	public RestTemplate restTemplate() {
		RequestConfig config = RequestConfig.custom().setConnectTimeout(300000).setConnectionRequestTimeout(300000)
				.setSocketTimeout(300000).build();

		CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(config)
				.setSSLHostnameVerifier(new NoopHostnameVerifier())
				.setRetryHandler(new DefaultHttpRequestRetryHandler(0, false)).setMaxConnTotal(200)
				.setMaxConnPerRoute(200).build();

		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
		ClientHttpRequestFactory factory = new BufferingClientHttpRequestFactory(requestFactory);
		RestTemplate restTemplate = new RestTemplate(factory);
		restTemplate.setInterceptors(Collections.singletonList(new RequestResponseLoggingInterceptor()));
		return restTemplate;
	}

}