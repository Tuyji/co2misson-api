package com.co2nsensus.co2mission.model.response.analytics;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AnalyticsModelList {
	private List<AnalyticsModel> analyticsModels;
}
