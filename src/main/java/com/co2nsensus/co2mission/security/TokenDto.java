package com.co2nsensus.co2mission.security;

import java.time.Instant;
import java.util.List;

public class TokenDto {



	private Long id;
	private String accessToken;
	private String refreshToken;
	private Instant createdAt;
	private Instant accessTokenExpiresAt;
	private Instant refreshTokenExpiresAt;
	private Boolean refreshed;
	private Instant refreshedAt;
	private Long profileId;
	private String ip;
	private String userAgent;
	private String refreshedIp;
	private String refreshedUserAgent;
	private List<String> roles;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getRefreshToken() {
		return refreshToken;
	}
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	public Instant getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}
	public Instant getAccessTokenExpiresAt() {
		return accessTokenExpiresAt;
	}
	public void setAccessTokenExpiresAt(Instant accessTokenExpiresAt) {
		this.accessTokenExpiresAt = accessTokenExpiresAt;
	}
	public Instant getRefreshTokenExpiresAt() {
		return refreshTokenExpiresAt;
	}
	public void setRefreshTokenExpiresAt(Instant refreshTokenExpiresAt) {
		this.refreshTokenExpiresAt = refreshTokenExpiresAt;
	}
	public Boolean getRefreshed() {
		return refreshed;
	}
	public void setRefreshed(Boolean refreshed) {
		this.refreshed = refreshed;
	}
	public Instant getRefreshedAt() {
		return refreshedAt;
	}
	public void setRefreshedAt(Instant refreshedAt) {
		this.refreshedAt = refreshedAt;
	}
	public Long getProfileId() {
		return profileId;
	}
	public void setProfileId(Long profileId) {
		this.profileId = profileId;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getUserAgent() {
		return userAgent;
	}
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
	public String getRefreshedIp() {
		return refreshedIp;
	}
	public void setRefreshedIp(String refreshedIp) {
		this.refreshedIp = refreshedIp;
	}
	public String getRefreshedUserAgent() {
		return refreshedUserAgent;
	}
	public void setRefreshedUserAgent(String refreshedUserAgent) {
		this.refreshedUserAgent = refreshedUserAgent;
	}
	public List<String> getRoles() {
		return roles;
	}
	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
	

}
