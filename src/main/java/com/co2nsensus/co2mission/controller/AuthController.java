package com.co2nsensus.co2mission.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.co2nsensus.co2mission.model.request.SigninRequest;
import com.co2nsensus.co2mission.model.response.AuthenticationResponse;
import com.co2nsensus.co2mission.security.TokenDto;
import com.co2nsensus.co2mission.service.impl.TokenService;

@RestController
@RequestMapping("/auth")
public class AuthController {

	private final TokenService tokenService;

	public AuthController(TokenService tokenService) {
		this.tokenService = tokenService;
	}

	@PostMapping("/signin")
	public AuthenticationResponse signIn(@RequestBody SigninRequest request, HttpServletRequest req) {
		System.out.println(request.getEmail() + "-" + request.getPassword() + "-" + request.getSecret() + "-"
				+ request.getClientId());
		TokenDto token = tokenService.issue(request.getClientId(), request.getSecret(), request.getEmail(),
				request.getPassword(), "", "");

		return new AuthenticationResponse(token.getAccessToken(), token.getRefreshToken(),
				token.getCreatedAt().getEpochSecond(), token.getAccessTokenExpiresAt().getEpochSecond());
	}

}
