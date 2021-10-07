package com.co2nsensus.co2mission.service.client.apis;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component("exchangeRatesAPI")
public class ExchangeRatesAPI {

	@Value("${app.exchangerate.endpoint}")
	private String conversionEndpoint;

	@Value("${app.exchangerate.apikey}")
	private String accessKey;

	public BigDecimal convert(String from, String to) throws JsonMappingException, JsonProcessingException {
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<String> response = restTemplate.getForEntity(
				UriComponentsBuilder.fromHttpUrl(conversionEndpoint).queryParam("access_key ", accessKey)
						.queryParam("from", from).queryParam("to", to).queryParam("amount", 1).toUriString(),
				String.class);
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(response.getBody());
		JsonNode info = root.path("info");
		JsonNode rate = info.get("rate");
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(rate.asText(), BigDecimal.class);

	}
}
