package com.co2nsensus.co2mission.service.co2nsenus.types;

import lombok.Data;

@Data
public class AccessTokenResponse {
	private String token;
	private String refresh_token;
}
