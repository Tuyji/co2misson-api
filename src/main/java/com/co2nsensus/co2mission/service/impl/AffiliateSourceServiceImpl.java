package com.co2nsensus.co2mission.service.impl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.co2nsensus.co2mission.model.entity.Affiliate;
import com.co2nsensus.co2mission.model.entity.platform.AffiliatePlatform;
import com.co2nsensus.co2mission.model.entity.source.AffiliateSource;
import com.co2nsensus.co2mission.model.enums.SourceType;
import com.co2nsensus.co2mission.model.response.platform.AffiliatePlatformModel;
import com.co2nsensus.co2mission.model.response.platform.PlatformModel;
import com.co2nsensus.co2mission.model.response.source.AffiliateSourceModel;
import com.co2nsensus.co2mission.model.response.source.AffiliateSourceModelList;
import com.co2nsensus.co2mission.repo.AffiliateSourceRepository;
import com.co2nsensus.co2mission.service.AffiliatePlatformService;
import com.co2nsensus.co2mission.service.AffiliateService;
import com.co2nsensus.co2mission.service.AffiliateSourceService;

@Service
public class AffiliateSourceServiceImpl implements AffiliateSourceService {

	private final AffiliateSourceRepository affiliateSourceRepository;
	private final AffiliateService affiliateService;
	private final AffiliatePlatformService affiliatePlatformService;

	public AffiliateSourceServiceImpl(AffiliateSourceRepository affiliateSourceRepository,
			AffiliateService affiliateService,AffiliatePlatformService affiliatePlatformService) {
		this.affiliateSourceRepository = affiliateSourceRepository;
		this.affiliateService = affiliateService;
		this.affiliatePlatformService = affiliatePlatformService;
	}

	@Override
	public AffiliateSourceModel addSource(String affiliateId, AffiliateSourceModel request) {
		
		Affiliate affiliate = affiliateService.getAffiliateEntityById(affiliateId);
		AffiliateSource source = new AffiliateSource();
		source.setAffiliate(affiliate);
		source.setAnalyticsName(request.getName().replaceAll(" ", "_").toUpperCase());
		source.setName(request.getName());
		source.setPlatform(affiliatePlatformService.getAffiliatePlatform(request.getPlatform().getId()));
		source.setType(request.getType());
		affiliateSourceRepository.save(source);
		request.setAnalyticsName(source.getAnalyticsName());
		return convert(source);

	}

	@Override
	public AffiliateSourceModelList getAffiliateSources(String affiliateId, SourceType sourceType) {
		List<AffiliateSource> affiliateSources = affiliateSourceRepository
				.findByAffiliateId(UUID.fromString(affiliateId));
		List<AffiliateSourceModel> affiliateSourceModels = affiliateSources.stream()
				.filter(s -> s.getType() == sourceType).map(s -> convert(s)).collect(Collectors.toList());
		return AffiliateSourceModelList.builder().sourceList(affiliateSourceModels).build();
	}

	private AffiliateSourceModel convert(AffiliateSource source) {
		AffiliateSourceModel sourceModel = new AffiliateSourceModel();
		sourceModel.setName(source.getName());
		sourceModel.setAnalyticsName(source.getAnalyticsName());
		sourceModel.setPlatform(new AffiliatePlatformModel(source.getPlatform().getId().toString(),
				new PlatformModel(source.getPlatform().getPlatform().getId().toString(),
						source.getPlatform().getPlatform().getName(), source.getPlatform().getPlatform().getUrl()),
				source.getPlatform().getDetail()));
		return sourceModel;
	}

	@Override
	public AffiliateSourceModelList getAllAffiliateSources(String affiliateId) {
		List<AffiliateSource> affiliateSources = affiliateSourceRepository
				.findByAffiliateId(UUID.fromString(affiliateId));
		List<AffiliateSourceModel> affiliateSourceModels = affiliateSources.stream().map(s -> convert(s))
				.collect(Collectors.toList());
		return AffiliateSourceModelList.builder().sourceList(affiliateSourceModels).build();
	}

}
