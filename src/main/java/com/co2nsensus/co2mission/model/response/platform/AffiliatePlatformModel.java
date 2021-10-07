package com.co2nsensus.co2mission.model.response.platform;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AffiliatePlatformModel {
	private String id;
    private PlatformModel platform;
    private String detail;

}
