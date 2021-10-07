package com.co2nsensus.co2mission.service.co2nsenus.types;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateChannelRequest {
	private String code;
	private String name;
	private String contactEmail;
	private Boolean isEnabled;
}
