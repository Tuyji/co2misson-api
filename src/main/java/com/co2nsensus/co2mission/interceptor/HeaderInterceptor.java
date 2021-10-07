package com.co2nsensus.co2mission.interceptor;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class HeaderInterceptor implements HandlerInterceptor {

	@Autowired
	HeaderRequestParameters requestParams;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		requestParams.setLanguageCode(request.getHeader(Header.LANGUAGE.code()));
		requestParams.setSessionId(request.getHeader(Header.SESSION_ID.code()));
		requestParams.setTransactionId(request.getHeader(Header.TRANSACTION_ID.code()));
		requestParams.setClientIp(request.getHeader(Header.CLIENT_IP.code()));
		requestParams.setDeviceId(request.getHeader(Header.DEVICE_ID.code()));
		requestParams.setxForwardFor(request.getHeader(Header.X_FORWARD_PROTO.code()));
		requestParams.setxForwardHost(request.getHeader(Header.X_FORWARD_HOST.code()));
		requestParams.setxForwardProto(request.getHeader(Header.X_FORWARD_FOR.code()));
		requestParams.setOperatingSystemId(request.getHeader(Header.OPERATING_SYSTEM_ID.code()));
		requestParams.setRequestTime(LocalDateTime.now());
		requestParams.setAdmin("TRUE".equals(request.getHeader(Header.IS_ADMIN.code())));

		return true;
	}
}
