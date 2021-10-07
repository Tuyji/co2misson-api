package com.co2nsensus.co2mission.model.response;

public class AuthenticationResponse {


	private String accessToken;
	private String tokenType = "Bearer";
	private String refreshToken;
	private Long issuedAt;
	private Long expiresAt;

	public AuthenticationResponse(String accessToken, String refreshToken, Long issuedAt, Long expiresAt) {
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.issuedAt = issuedAt;
		this.expiresAt = expiresAt;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public Long getIssuedAt() {
		return issuedAt;
	}

	public void setIssuedAt(Long issuedAt) {
		this.issuedAt = issuedAt;
	}

	public Long getExpiresAt() {
		return expiresAt;
	}

	public void setExpiresAt(Long expiresAt) {
		this.expiresAt = expiresAt;
	}


}
