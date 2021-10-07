package com.co2nsensus.co2mission.interceptor;

import java.io.IOException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingResponseWrapper;

import com.co2nsensus.co2mission.model.dto.logging.Correlation;
import com.co2nsensus.co2mission.model.dto.logging.Performance;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Component
@Order(1)
@Slf4j
public class RequestResponseLoggingFilter implements Filter {

	public static final String REQUEST_HEADER_NAME = "X-Correlation-Id";

	public static final String REQUEST_MSISDN = "msisdn";

	public static int MAX_CONTENT_LENGTH = 1048576; // 1 MB //

	@Autowired
	ObjectMapper objectMapper;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
			log.info(
					"only HttpServletRequest and HttpServletResponse type are supported. Ignoring request and skipping.");
			chain.doFilter(request, response);
		} else {
			HttpServletRequest servletRequest = (HttpServletRequest) request;
			if (servletRequest.getRequestURI().length() < 6)
				chain.doFilter(request, response);
			if ("/docs".equals(servletRequest.getRequestURI().substring(0, 5))
					|| (servletRequest.getRequestURI().contains("verification/files")
							&& servletRequest.getMethod().equals("POST"))
					|| "/health".equals(servletRequest.getRequestURI().substring(0, 7))
					|| "/actuator".equals(servletRequest.getRequestURI().substring(0, 9))
					|| "/swagger".equals(servletRequest.getRequestURI().substring(0, 8))
					|| servletRequest.getRequestURI().contains("/verification/files/type")
					|| "/v2/api-docs".equals(servletRequest.getRequestURI().substring(0, 12))
					|| "OPTIONS".equals(servletRequest.getMethod())
					|| servletRequest.getContentLengthLong() > MAX_CONTENT_LENGTH
					|| servletRequest.getRequestURI().contains("static")) {
				chain.doFilter(request, response);
			} else {
				String requestCorId = Correlation.getNewCorrelationId();
				String msisdn = null;
				Performance perf = new Performance(requestCorId);
				Co2missionRequestWrapper requestWrapper = new Co2missionRequestWrapper(servletRequest);
				ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(
						(HttpServletResponse) response);
				HttpServletRequest req = (HttpServletRequest) request;
				HttpServletResponse res = (HttpServletResponse) response;
				String correlationId = req.getHeader(REQUEST_HEADER_NAME);
				if (correlationId == null) {
					correlationId = requestCorId;
				}
				requestWrapper.setAttribute(REQUEST_HEADER_NAME, correlationId);
				String requestBody = requestWrapper.getBody();
				String uri = req.getRequestURI();

				StringBuilder requestString = new StringBuilder("\"requestUri\":\"").append(uri)
						.append("\",\"requestType\":\"").append(req.getMethod()).append("\",\"reqHeaders\":")
						.append(formatReqHeaders(req)).append(",\"requestBody\":")
						.append(requestBody.replace("\r", " ").replace("\n", " "));

				log.info(
						"\"LoggingFilterRequest\":{ \"requestId\":\"{}\", \"duration\":, \"msisdn\":\"\", \"performanceValues\":{},"
								+ requestString.toString() + " \"statusCode\":}",
						requestCorId, objectMapper.writeValueAsString(perf));
				log.info("RequestToAppender: requestId:{}, duration: {}, msisdn:{}, requestUri: {}", requestCorId, "",
						"", uri);

				if (res.getHeader(REQUEST_HEADER_NAME) == null) {
					responseWrapper.setHeader(REQUEST_HEADER_NAME, correlationId);
				}

				try {
					chain.doFilter(requestWrapper, responseWrapper);
				} finally {
					Object o = requestWrapper.getAttribute(REQUEST_MSISDN);
					if (o != null)
						msisdn = ((String) o).toString();
					String responseBody = new String(responseWrapper.getContentAsByteArray());
					if (responseBody != null && responseBody.length() > MAX_CONTENT_LENGTH) {
						responseBody = responseBody.substring(0, MAX_CONTENT_LENGTH);
					}
					// Do not forget this line after reading response content or actual response
					// will be empty!
					responseWrapper.copyBodyToResponse();
					perf.setResponseTimestamp();

					// Write request and response body, headers, timestamps etc. to log files

					log.info(
							"\"LoggingFilterResponse\":{\"requestId\":\"{}\", \"duration\":{}, \"msisdn\":\"{}\", \"performanceValues\":{}, "
									+ requestString.toString()
									+ ", \"respHeaders\":{}, \"responseBody\":{}, \"statusCode\":{}}",
							requestCorId, perf.getDuration(), msisdn, objectMapper.writeValueAsString(perf),
							formatRespHeaders(res), responseBody.replace("\r", " ").replace("\n", " "),
							responseWrapper.getStatusCode());
					log.info("ResponseToAppender: requestId:{}, duration: {}, msisdn:{}, requestUri: {}", requestCorId,
							perf.getDuration(), msisdn, uri);

				}
			}
		}
	}

	@Override
	public void destroy() {
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}

	public String format(final Map<String, Object> content) {
		return content.entrySet().stream().map(entry -> String.format("%s=%s", entry.getKey(), entry.getValue()))
				.collect(Collectors.joining(" "));
	}

	private String formatReqHeaders(final HttpServletRequest request) throws JsonProcessingException {
		Enumeration<String> headerNames = request.getHeaderNames();
		Map<String, String> headers = new HashMap<>();
		while (headerNames.hasMoreElements()) {
			String key = headerNames.nextElement();
			headers.put(key, request.getHeader(key));
		}
		return objectMapper.writeValueAsString(headers);
	}

	private String formatRespHeaders(final HttpServletResponse response) throws JsonProcessingException {

		Collection<String> headerNames = response.getHeaderNames();
		Map<String, String> headers = new HashMap<>();
		headerNames.forEach((k) -> headers.put(k, response.getHeader(k)));
		return objectMapper.writeValueAsString(headers);

	}
}