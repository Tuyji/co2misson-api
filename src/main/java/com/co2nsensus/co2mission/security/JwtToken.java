package com.co2nsensus.co2mission.security;

import java.time.Instant;
import java.util.List;

public class JwtToken {
	private Long id;
	private String token;
	private Instant expiresAt;
	private JwtTokenType type;
	private Instant issuedAt;
	private List<String> roles;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Instant getExpiresAt() {
		return expiresAt;
	}
	public void setExpiresAt(Instant expiresAt) {
		this.expiresAt = expiresAt;
	}
	public JwtTokenType getType() {
		return type;
	}
	public void setType(JwtTokenType type) {
		this.type = type;
	}
	public Instant getIssuedAt() {
		return issuedAt;
	}
	public void setIssuedAt(Instant issuedAt) {
		this.issuedAt = issuedAt;
	}
	public List<String> getRoles() {
		return roles;
	}
	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
	
	
}
