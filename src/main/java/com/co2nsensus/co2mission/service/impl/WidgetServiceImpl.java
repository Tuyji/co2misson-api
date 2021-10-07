package com.co2nsensus.co2mission.service.impl;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.co2nsensus.co2mission.model.entity.Affiliate;
import com.co2nsensus.co2mission.model.entity.AffiliateWidget;
import com.co2nsensus.co2mission.model.response.widget.AffiliateWidgetModel;
import com.co2nsensus.co2mission.service.AffiliateService;
import com.co2nsensus.co2mission.service.WidgetService;
import com.co2nsensus.co2mission.service.co2nsenus.Co2nsensusInterface;
import com.co2nsensus.co2mission.service.co2nsenus.types.CreateWidgetRequest;
import com.co2nsensus.co2mission.service.co2nsenus.types.CreateWidgetResponse;

@Service
public class WidgetServiceImpl implements WidgetService {

	private final AffiliateService affiliateService;

	private final Co2nsensusInterface co2nsensusInterface;

	public WidgetServiceImpl(AffiliateService affiliateService, Co2nsensusInterface co2nsensusInterface) {
		this.affiliateService = affiliateService;
		this.co2nsensusInterface = co2nsensusInterface;
	}

	@Override
	public List<AffiliateWidgetModel> getAffiliateWidgets(String affiliateId) {
		Affiliate affiliate = affiliateService.getAffiliateEntityById(affiliateId);
		return affiliate.getAffiliateWidgets().stream().map(w -> AffiliateWidgetModel.builder()
				.externalId(w.getExternalId()).host(w.getHost()).id(w.getId()).theme(w.getTheme()).build())
				.collect(Collectors.toList());
	}

	@Override
	public AffiliateWidgetModel createWidget(String affiliateId, AffiliateWidgetModel widgetModel) {
		Affiliate affiliate = affiliateService.getAffiliateEntityById(affiliateId);
		CreateWidgetRequest createWidgetRequest = new CreateWidgetRequest();
		createWidgetRequest.setChannel(affiliate.getChannelId());
		createWidgetRequest.setWidgetUrl(widgetModel.getHost());
		CreateWidgetResponse createWidgetResponse = co2nsensusInterface.createWidget(createWidgetRequest);
		AffiliateWidget affiliateWidget = AffiliateWidget.builder().affiliate(affiliate).createdAt(LocalDateTime.now())
				.externalId(createWidgetResponse.getId()).theme(widgetModel.getTheme()).host(widgetModel.getHost())
				.build();
		Set<AffiliateWidget> affiliateWidgets = affiliate.getAffiliateWidgets();
		if (affiliateWidgets == null) {
			affiliateWidgets = new HashSet<>();
			affiliateWidgets.add(affiliateWidget);
			affiliate.setAffiliateWidgets(affiliateWidgets);
		} else {
			affiliate.getAffiliateWidgets().add(affiliateWidget);
		}

		affiliate = affiliateService.saveAffiliate(affiliate);
		widgetModel.setExternalId(createWidgetResponse.getId());
		Optional<AffiliateWidget> widgetOptional = affiliate.getAffiliateWidgets().stream()
				.filter(w -> w.getExternalId().equals(widgetModel.getExternalId())).findFirst();
		if (widgetOptional.isPresent()) {
			widgetModel.setId(widgetOptional.get().getId());
		}
		return widgetModel;
	}

}
