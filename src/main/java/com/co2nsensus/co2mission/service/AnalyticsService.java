package com.co2nsensus.co2mission.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.co2nsensus.co2mission.model.entity.Affiliate;
import com.co2nsensus.co2mission.model.request.AnalyticsRequest;
import com.co2nsensus.co2mission.model.response.analytics.AnalyticsModel;

public interface AnalyticsService {
	List<AnalyticsModel> getAnalytics(AnalyticsRequest request);

	int getClickCount(String affiliateId, String source, LocalDateTime startDate, LocalDateTime endDate);

	Map<String, AnalyticsModel> getClickAndPurchaseCounts(List<Affiliate> affiliateList, LocalDateTime startDate,
			LocalDateTime endDate,String source);
}
