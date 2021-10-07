package com.co2nsensus.co2mission.model.response.source;

import com.co2nsensus.co2mission.model.enums.SourceType;
import com.co2nsensus.co2mission.model.response.platform.AffiliatePlatformModel;
import com.co2nsensus.co2mission.model.response.platform.PlatformModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AffiliateSourceModel {
	private String name;
	private String analyticsName;
	private SourceType type;
	private AffiliatePlatformModel platform;
	private String widgetId;
}
