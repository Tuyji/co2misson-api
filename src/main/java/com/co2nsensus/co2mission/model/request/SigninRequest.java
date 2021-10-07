package com.co2nsensus.co2mission.model.request;

public class SigninRequest {


	private String email;
	private String password;
	private String clientId;
	private String secret;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	@Override
	public String toString() {
		return "SigninRequest [email=" + email + ", password=" + password + ", clientId=" + clientId + ", secret="
				+ secret + "]";
	}
	
	


}
