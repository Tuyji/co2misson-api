package com.co2nsensus.co2mission.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.co2nsensus.co2mission.exception.AffiliateRuntimeException;
import com.google.common.base.Charsets;
import com.google.common.io.ByteSource;
import com.google.common.io.ByteStreams;

public class HttpUtils {

	private static final Logger LOG = LoggerFactory.getLogger(HttpUtils.class);

	private HttpUtils() {
		throw new IllegalAccessError("Utility class");
	}

	public static String getRequestHeaderAsString(HttpServletRequest request) {
		String requestParameters = "";
		Enumeration<String> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String key = headerNames.nextElement();
			if (!StringUtils.isAllBlank(request.getHeader(key))) {
				requestParameters += "- " + key + " - " + request.getHeader(key) + " ";
			}
		}
		return requestParameters;
	}

	public static String getRequestAsString(HttpServletRequest request) {
		String requestParameters = request.getRequestURI() + "\n";
		if (!"GET".equals(request.getMethod())) {
			try {
				requestParameters = ByteSource.wrap(ByteStreams.toByteArray(request.getInputStream()))
						.asCharSource(Charsets.UTF_8).read();
			} catch (IOException e) {
				LOG.error(exception2String(e));
			}
		} else {
			Enumeration<String> paramEnum = request.getParameterNames();
			while (paramEnum.hasMoreElements()) {
				requestParameters += request.getParameter(paramEnum.nextElement()) + "-";
			}
		}
		return requestParameters;
	}

	public static String exception2String(Exception e) {
		StringWriter writer = new StringWriter();
		e.printStackTrace(new PrintWriter(writer));
		return writer.toString();
	}

	public static String exception2String(AffiliateRuntimeException e) {
		StringWriter writer = new StringWriter();
		e.printStackTrace(new PrintWriter(writer));
		return writer.toString();
	}

}
