package com.co2nsensus.co2mission.model.request;

import com.co2nsensus.co2mission.model.enums.AnalyticsBreakdownType;
import com.co2nsensus.co2mission.model.enums.AnalyticsDateInterval;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AnalyticsRequest {
	private AnalyticsBreakdownType breakDown;
	private AnalyticsDateInterval interval;
	private String customIntervalFrom;
	private String customIntervalTo;
	private String affiliateId;
}
