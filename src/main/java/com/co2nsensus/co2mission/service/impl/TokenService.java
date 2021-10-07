package com.co2nsensus.co2mission.service.impl;

import java.time.Instant;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.co2nsensus.co2mission.auth.UserPrincipal;
import com.co2nsensus.co2mission.security.JwtToken;
import com.co2nsensus.co2mission.security.JwtTokenProvider;
import com.co2nsensus.co2mission.security.JwtTokenType;
import com.co2nsensus.co2mission.security.TokenDto;

@Transactional
@Component
public class TokenService {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	JwtTokenProvider tokenProvider;

//	@Autowired
//	ClientRepo clientRepository;
//
//	@Autowired
//	ExceptionService exceptionService;

	@Value("${app.jwtSecret}")
	private String jwtSecret;

	@Value("${app.activationTokenExpirationInMs}")
	private String expiry;

	public TokenDto issue(String clientId, String secret, String username, String password, String ip,
			String userAgent) {
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

		return issue(clientId, secret, userPrincipal, ip, userAgent, JwtTokenType.AT);
	}

	private TokenDto issue(String clientId, String secret, UserPrincipal userPrincipal, String ip, String userAgent,
			JwtTokenType type) {

//		Client client = clientRepository.findByClientIdAndSecret(clientId, secret);
//		if (client == null) {
//			exceptionService.throwCoreException(ExceptionList.BAD_CREDENTIALS);
//		}
//		Long id = tokenRepository.getNextSequence();
		Long id = 1l;
		Instant now = Instant.now();
//		JwtToken accessToken = tokenProvider.generateToken(userPrincipal, now, type, null, clientId,
//				client.getAccessTokenExpiryMs(), client.getRefreshTokenExpiryMs());
		JwtToken accessToken = tokenProvider.generateToken(userPrincipal, now, type, null, clientId,
				Long.valueOf(expiry), Long.valueOf(expiry));
//		accessToken.setId(id);
		JwtToken refreshToken = tokenProvider.generateToken(userPrincipal, now, JwtTokenType.RT, accessToken, clientId,
				Long.valueOf(expiry), Long.valueOf(expiry));
		TokenDto _token = new TokenDto();
		_token.setAccessToken(accessToken.getToken());
		_token.setRefreshToken(refreshToken.getToken());
		_token.setCreatedAt(now);
		_token.setAccessTokenExpiresAt(accessToken.getExpiresAt());
		_token.setRoles(accessToken.getRoles());
		return _token;
	}

}
