package com.co2nsensus.co2mission.exception;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.co2nsensus.co2mission.auth.AuthenticationFacade;
import com.co2nsensus.co2mission.utils.HttpUtils;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {

	private final AuthenticationFacade authentication;

	public CustomGlobalExceptionHandler(AuthenticationFacade authentication) {
		this.authentication = authentication;
	}

	@ExceptionHandler(AffiliateCustomNotFoundException.class)
	public ResponseEntity<?> resourceNotFoundException(AffiliateCustomNotFoundException ex,
			HttpServletRequest request) {
		CustomErrorResponse errorDetails = new CustomErrorResponse(new Date(), ex.getCode(), ex.getMessage());
		String endPoint = request.getServletPath().substring(1);
		String exceptionString = HttpUtils.exception2String(ex);
		String userId = "";
		try {
			userId = authentication.getUserPrincipal().getId();
		} catch (Exception exc) {
			userId = "Anonymous";
		}
		log.error("-EndPoint:{}- HttpMethod:{}\n- UserId:{} - \n- Exception:{} \n- Request:{}", endPoint,
				request.getMethod(), userId, exceptionString, HttpUtils.getRequestAsString(request));

		return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(AffiliateCustomInternalServerException.class)
	public ResponseEntity<?> internalServerError(AffiliateCustomInternalServerException ex, WebRequest request) {
		CustomErrorResponse errorDetails = new CustomErrorResponse(new Date(), ex.getCode(), ex.getMessage());
		return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	// ( @Validate For Validating Path Variables and Request Parameters
	@ExceptionHandler(ConstraintViolationException.class)
	public void constraintViolationException(HttpServletResponse response) throws IOException {
		response.sendError(HttpStatus.BAD_REQUEST.value());
	}

}