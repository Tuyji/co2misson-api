package com.co2nsensus.co2mission.security;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import com.co2nsensus.co2mission.auth.UserPrincipal;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenProvider {

//	private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

	@Value("${app.jwtSecret}")
	private String jwtSecret;

	public JwtToken generateToken(UserPrincipal userPrincipal, Instant instant, JwtTokenType tokenType,
			JwtToken accessToken, String clientId, Long jwtExpirationInMs, Long jwtRefreshExpirationInMs) {
		Date expiryDate = null;
		Map<String, Object> claims = new HashMap<>();
		JwtToken jwtToken = new JwtToken();
		if (tokenType == JwtTokenType.AT || tokenType == JwtTokenType.ST) {
			claims.put("em", userPrincipal.getEmail());
			claims.put("tt", tokenType);
			claims.put("roles", AuthorityUtils.authorityListToSet(userPrincipal.getAuthorities()));

			expiryDate = new Date(instant.toEpochMilli() + jwtExpirationInMs);
			String token = Jwts.builder().setClaims(claims).setSubject(userPrincipal.getId())
					.setIssuedAt(new Date(instant.toEpochMilli())).setExpiration(expiryDate)
					.signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
			jwtToken.setToken(token);
			jwtToken.setExpiresAt(expiryDate.toInstant());
			jwtToken.setType(tokenType);
			jwtToken.setIssuedAt(instant);
			jwtToken.setRoles(getRoleList(userPrincipal));
		} else if (tokenType == JwtTokenType.RT) {
			claims.put("tt", tokenType);

			expiryDate = new Date(instant.toEpochMilli() + jwtRefreshExpirationInMs);
			String token = Jwts.builder().setClaims(claims).setIssuedAt(new Date(instant.toEpochMilli()))
					.setExpiration(expiryDate).setId(String.valueOf(accessToken.getId()))
					.signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
			jwtToken.setToken(token);
			jwtToken.setExpiresAt(expiryDate.toInstant());
			jwtToken.setType(tokenType);
			jwtToken.setIssuedAt(instant);
		} else {

		}
		return jwtToken;

	}

	public Claims parse(String token) {
		Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
		return claims;
	}

	public void validateToken(String token) {
		try {
			parse(token);
		} catch (Exception ex) {
//			logger.error("Token Parse Error: " + token, ex);
//			throw new CoreException("0000", "Access Token Invalid");
		}
	}

	private List<String> getRoleList(UserPrincipal userPrincipal) {
		List<String> list = new ArrayList<>();

		for (GrantedAuthority authority : userPrincipal.getAuthorities()) {
			list.add(authority.getAuthority());
		}

		return list;
	}
}
