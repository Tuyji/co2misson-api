package com.co2nsensus.co2mission.model.enums;

public enum AnalyticsBreakdownType {
	GENDER("ga:userGender"),LOCATION("ga:country"),DEVICE("ga:deviceCategory"),AGE("ga:userAgeBracket");
	
	private String gaName;
	private AnalyticsBreakdownType(String gaName) {
		this.gaName = gaName;
	}
	
	
	public String gaName() {
		return gaName;
	}
	
}
