package com.co2nsensus.co2mission.service.co2nsenus.types;

import lombok.Data;

@Data
public class CreateWidgetRequest {
	private String channel;
	private String widgetUrl;
}
