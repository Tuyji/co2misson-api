package com.co2nsensus.co2mission.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

	private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e)
			throws IOException, ServletException {
		logger.error("Responding with unauthorized error. Message - {}", e.getMessage());

		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType("application/json");
		response.getWriter().write(getMessage(e));
		response.getWriter().flush();
	}

	public String getMessage(AuthenticationException ae) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode objectNode1 = mapper.createObjectNode();
		ae.printStackTrace();
		if (ae instanceof BadCredentialsException) {
			objectNode1.put("code", "0001");
			objectNode1.put("message", "Bad Credentials");
		} else {
			objectNode1.put("code", "0002");
			objectNode1.put("message", "Access Token Required/Invalid/Expired");
		}

		try {
			return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectNode1);
		} catch (JsonProcessingException e) {
			return "";
		}
	}

}