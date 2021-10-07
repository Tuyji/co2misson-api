package com.co2nsensus.co2mission.interceptor;

import java.io.IOException;
import java.nio.charset.Charset;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

import com.co2nsensus.co2mission.model.dto.logging.Correlation;
import com.co2nsensus.co2mission.model.dto.logging.Performance;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RequestResponseLoggingInterceptor implements ClientHttpRequestInterceptor {

	public static final String REQUEST_HEADER_NAME = "X-Correlation-Id";

	public static int MAX_CONTENT_LENGTH = 1048576; // 1 MB //

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
			throws IOException {
		if (body != null && body.length > MAX_CONTENT_LENGTH) {
			return execution.execute(request, body);
		} else {
			String requestCorId = request.getHeaders().getFirst(REQUEST_HEADER_NAME);
			if (requestCorId == null) {
				requestCorId = Correlation.getNewCorrelationId();
			}
			Performance perf = new Performance(requestCorId);
			String requestBody = new String(body, "UTF-8");
			logRequest(request, requestBody, requestCorId);
			ClientHttpResponse response = execution.execute(request, body);
			perf.setResponseTimestamp();
			logResponse(request, requestBody, response, requestCorId, perf);
			return response;
		}
	}

	private void logRequest(HttpRequest request, String requestBody, String correlationId) throws IOException {

		log.info(
				"RestRequest: RequestId: {}, duration:{}, URI:{}, Method: {}, StatusCode: {}, Headers: {}, RequestBody: {}",
				correlationId, "0", request.getURI(), request.getMethod(), "", request.getHeaders(), requestBody);
		log.info("AppRestRequest: requestId:{}, duration: {}, statusCode:{}, uri: {}", correlationId, 0, "",
				request.getURI());

	}

	private void logResponse(HttpRequest request, String requestBody, ClientHttpResponse response, String correlationId,
			Performance perf) throws IOException {
		String responseBody = StreamUtils.copyToString(response.getBody(), Charset.defaultCharset());
		if (responseBody != null && responseBody.length() > MAX_CONTENT_LENGTH) {
			responseBody = responseBody.substring(0, MAX_CONTENT_LENGTH);
		}

		log.info(
				"RestResponse: RequestId : {}, duration:{}, URI:{}, StatusCode: {}, Method: {}, Headers: {}, RequestBody: {}, performance: {}, StatusText: {}, ResponseHeaders: {}, ResponseBody: {}",
				correlationId, perf.getDuration(), request.getURI(), response.getStatusCode(), request.getMethod(),
				request.getHeaders(), requestBody, perf.toString(), response.getStatusText(), response.getHeaders(),
				responseBody);
		log.info("AppRestResponse: requestId:{}, duration: {}, statusCode:{}, uri: {}", correlationId,
				perf.getDuration(), response.getStatusCode(), request.getURI());
	}

}