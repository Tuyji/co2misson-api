package com.co2nsensus.co2mission.interceptor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class HeaderRequestParameters {
	private String languageCode;
	private String sessionId;
	private String transactionId;
	private String clientIp;
	private String deviceId;
	private String xForwardProto;
	private String xForwardHost;
	private String xForwardFor;
	private String operatingSystemId;
	private LocalDateTime requestTime;
	private boolean isAdmin;

	public LocalDateTime getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(LocalDateTime requestTime) {
		this.requestTime = requestTime;
	}


	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getLanguageCode() {
		return languageCode;
	}

	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getClientIp() {
		return clientIp;
	}

	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}

	public String getxForwardProto() {
		return xForwardProto;
	}

	public void setxForwardProto(String xForwardProto) {
		this.xForwardProto = xForwardProto;
	}

	public String getxForwardHost() {
		return xForwardHost;
	}

	public void setxForwardHost(String xForwardHost) {
		this.xForwardHost = xForwardHost;
	}

	public String getxForwardFor() {
		return xForwardFor;
	}

	public void setxForwardFor(String xForwardFor) {
		this.xForwardFor = xForwardFor;
	}


	public String getOperatingSystemId() {
		return operatingSystemId;
	}

	public void setOperatingSystemId(String operatingSystemId) {
		this.operatingSystemId = operatingSystemId;
	}

	

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public Map<String, String> getRequestParamMap() {
		Map<String, String> requestParameters = new HashMap<>();
		if (StringUtils.isNoneBlank(languageCode)) {
			requestParameters.put(Header.LANGUAGE.code(), languageCode);
		}

		if (StringUtils.isNoneBlank(sessionId)) {
			requestParameters.put(Header.SESSION_ID.code(), sessionId);
		}
		if (StringUtils.isNoneBlank(operatingSystemId)) {
			requestParameters.put(Header.OPERATING_SYSTEM_ID.code(), operatingSystemId);
		}
		if (StringUtils.isNoneBlank(deviceId)) {
			requestParameters.put(Header.DEVICE_ID.code(), deviceId);
		}

		if (StringUtils.isNoneBlank(transactionId)) {
			requestParameters.put(Header.TRANSACTION_ID.code(), transactionId);
		}

		if (StringUtils.isNoneBlank(clientIp)) {
			requestParameters.put(Header.CLIENT_IP.code(), clientIp);
		}
		if (StringUtils.isNoneBlank(xForwardProto)) {
			requestParameters.put(Header.X_FORWARD_PROTO.code(), xForwardProto);
		}
		if (StringUtils.isNoneBlank(xForwardHost)) {
			requestParameters.put(Header.X_FORWARD_HOST.code(), xForwardHost);
		}
		if (StringUtils.isNoneBlank(xForwardFor)) {
			requestParameters.put(Header.X_FORWARD_FOR.code(), xForwardFor);
		}

		requestParameters.put("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);

		return requestParameters;
	}

	@Override
	public String toString() {
		return "HeaderRequestParameters [languageCode=" + languageCode + ", sessionId=" + sessionId + ", transactionId="
				+ transactionId + ", clientIp=" + clientIp + ", deviceId=" + deviceId + ", xForwardProto="
				+ xForwardProto + ", xForwardHost=" + xForwardHost + ", xForwardFor=" + xForwardFor
				+ ", operatingSystemId=" + operatingSystemId + ", requestTime=" + requestTime + ", isAdmin=" + isAdmin
				+ "]";
	}

	
}