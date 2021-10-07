package com.co2nsensus.co2mission.service;

import java.util.List;

import com.co2nsensus.co2mission.model.response.widget.AffiliateWidgetModel;

public interface WidgetService {

	List<AffiliateWidgetModel> getAffiliateWidgets(String affiliateId);
	
	AffiliateWidgetModel createWidget(String affiliateId,AffiliateWidgetModel widget);
}
