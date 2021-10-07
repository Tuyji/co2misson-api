package com.co2nsensus.co2mission.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.co2nsensus.co2mission.exception.AffiliateCustomNotFoundException;
import com.co2nsensus.co2mission.exception.AffiliateErrorCodes;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.co2nsensus.co2mission.exception.PlatformNotFoundException;
import com.co2nsensus.co2mission.model.entity.Affiliate;
import com.co2nsensus.co2mission.model.entity.platform.AffiliatePlatform;
import com.co2nsensus.co2mission.model.entity.platform.Platform;
import com.co2nsensus.co2mission.model.response.affiliate.AffiliateModel;
import com.co2nsensus.co2mission.model.response.platform.AffiliatePlatformModel;
import com.co2nsensus.co2mission.model.response.platform.AffiliatePlatformModelList;
import com.co2nsensus.co2mission.model.response.platform.PlatformModel;
import com.co2nsensus.co2mission.repo.platform.AffiliatePlatformRepository;
import com.co2nsensus.co2mission.repo.platform.PlatformRepository;
import com.co2nsensus.co2mission.service.AffiliatePlatformService;
import com.co2nsensus.co2mission.service.AffiliateService;
import com.co2nsensus.co2mission.utils.ListUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AffiliatePlatformServiceImpl implements AffiliatePlatformService {

	private final AffiliatePlatformRepository affiliatePlatformRepository;

	private final AffiliateService affiliateService;

	private final PlatformRepository platformRepository;

	public AffiliatePlatformServiceImpl(AffiliatePlatformRepository affiliatePlatformRepository,
			AffiliateService affiliateService, PlatformRepository platformRepository) {
		this.affiliatePlatformRepository = affiliatePlatformRepository;
		this.affiliateService = affiliateService;
		this.platformRepository = platformRepository;
	}

	@Override
	public AffiliatePlatformModelList getPlatformsByAffiliateId(String affiliateId) {
		log.info("AffiliatePlatformServiceImpl.getPlatformsByAffiliateId started");
		AffiliateModel affiliateModel = affiliateService.getAffiliateById(affiliateId);
		List<AffiliatePlatformModel> platforms = Optional.ofNullable(affiliateModel.getPlatforms())
				.orElseGet(Collections::emptyList).stream().collect(Collectors.toList());

		ListUtils<AffiliatePlatformModel> utils = BeanUtils.instantiate(ListUtils.class);

		List<AffiliatePlatformModel> affiliatePlatformModels = new ArrayList<>();
		utils.copyList(platforms, affiliatePlatformModels, AffiliatePlatformModel.class);
		log.info("AffiliatePlatformServiceImpl.getPlatformsByAffiliateId finished");

		return AffiliatePlatformModelList.builder().affiliatePlatformModels(affiliatePlatformModels).build();
	}

	@Override
	public AffiliatePlatformModel addPlatform(String affiliateId, AffiliatePlatformModel affiliatePlatformModel) {
		AffiliatePlatform affiliatePlatform = new AffiliatePlatform();
		Affiliate affiliate = affiliateService.getAffiliateEntityById(affiliateId);
		affiliatePlatform.setAffiliate(affiliate);
		affiliatePlatform.setDetail(affiliatePlatformModel.getDetail());
		Platform platform = platformRepository.findById(UUID.fromString(affiliatePlatformModel.getPlatform().getId()))
				.orElseThrow(() -> new PlatformNotFoundException(AffiliateErrorCodes.PLATFORM_NOT_FOUND.getCode(),
						AffiliateErrorCodes.PLATFORM_NOT_FOUND.getMessage()));
		affiliatePlatform.setPlatform(platform);
		affiliatePlatform = affiliatePlatformRepository.save(affiliatePlatform);
		affiliatePlatformModel.setId(affiliatePlatform.getId().toString());
		affiliatePlatformModel.getPlatform().setUrl(platform.getUrl());
		affiliatePlatformModel.getPlatform().setLabel(platform.getName());
		return affiliatePlatformModel;
	}

	@Override
	public void deletePlatform(String affiliateId, String affiliatePlatformId) {
		affiliatePlatformRepository.deleteById(UUID.fromString(affiliatePlatformId));
	}

	@Override
	public List<PlatformModel> getPlatforms() {
		return platformRepository.findAll().stream()
				.filter(p -> p.getIsActive() != null && p.getIsActive().booleanValue())
				.map(p -> new PlatformModel(p.getId().toString(), p.getName(), p.getUrl()))
				.collect(Collectors.toList());
	}

	@Override
	public AffiliatePlatform getAffiliatePlatform(String affiliatePlatformId) {
		return affiliatePlatformRepository.findById(UUID.fromString(affiliatePlatformId)).orElseThrow(
				() -> new AffiliateCustomNotFoundException(AffiliateErrorCodes.PLATFORM_NOT_FOUND.getCode(),
						AffiliateErrorCodes.PLATFORM_NOT_FOUND.getMessage()));

	}

}
