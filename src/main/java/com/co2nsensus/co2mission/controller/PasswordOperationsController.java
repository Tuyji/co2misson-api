package com.co2nsensus.co2mission.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.co2nsensus.co2mission.model.request.ResetPasswordRequestModel;

public class PasswordOperationsController {
//	@PostMapping("/forgot-password")
//	public ResponseEntity<?> forgotPassword(HttpServletRequest servletRequest, @RequestParam String email) {
//		String response = affiliateService.processForgotPassword(servletRequest, email);
//		return new ResponseEntity<>(response, HttpStatus.OK);
//	}
//
//	@PostMapping("/reset-password")
//	public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequestModel requestModel) {
//		String response = affiliateService.processResetPassword(requestModel);
//		return new ResponseEntity<>(response, HttpStatus.OK);
//	}
}
