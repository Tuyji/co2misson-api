package com.co2nsensus.co2mission.interceptor;

public enum Header {
	LANGUAGE("lang"),SESSION_ID("session-id"), TRANSACTION_ID("transaction-id"), DEVICE_ID("device-id"),
	OPERATING_SYSTEM_ID("operating-system-id"), CLIENT_IP("client-ip"), X_FORWARD_PROTO("x-forward-proto"),
	X_FORWARD_HOST("x-forward-host"), X_FORWARD_FOR("x-forward-for"),IS_ADMIN("is-admin");

	private String code;

	private Header(String code) {
		this.code = code;
	}

	public String code() {
		return code;
	}

}
