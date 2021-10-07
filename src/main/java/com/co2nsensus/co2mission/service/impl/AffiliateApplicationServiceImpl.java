package com.co2nsensus.co2mission.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.co2nsensus.co2mission.exception.AffiliateErrorCodes;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.co2nsensus.co2mission.exception.AffiliateNotFoundException;
import com.co2nsensus.co2mission.model.entity.AffiliateApplication;
import com.co2nsensus.co2mission.model.entity.City;
import com.co2nsensus.co2mission.model.entity.platform.AffiliatePlatform;
import com.co2nsensus.co2mission.model.entity.platform.Platform;
import com.co2nsensus.co2mission.model.enums.ApplicationStatus;
import com.co2nsensus.co2mission.model.request.AffiliateApplicationRequest;
import com.co2nsensus.co2mission.model.response.application.AffiliateApplicationModel;
import com.co2nsensus.co2mission.model.response.application.AffiliateApplicationModelList;
import com.co2nsensus.co2mission.model.response.location.CityModel;
import com.co2nsensus.co2mission.model.response.location.CountryModel;
import com.co2nsensus.co2mission.model.response.platform.AffiliatePlatformModel;
import com.co2nsensus.co2mission.model.response.platform.PlatformModel;
import com.co2nsensus.co2mission.repo.AffiliateApplicationRepository;
import com.co2nsensus.co2mission.service.AffiliateApplicationService;
import com.co2nsensus.co2mission.service.AffiliatePlatformService;
import com.co2nsensus.co2mission.service.AffiliateService;
import com.co2nsensus.co2mission.service.co2nsenus.Co2nsensusInterface;
import com.co2nsensus.co2mission.service.co2nsenus.types.CreateChannelRequest;
import com.co2nsensus.co2mission.service.co2nsenus.types.CreateChannelResponse;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AffiliateApplicationServiceImpl implements AffiliateApplicationService {

	private final AffiliateService affiliateService;
	private final AffiliateApplicationRepository affiliateApplicationRepo;
	private final Co2nsensusInterface co2nsensusInterface;
	private final PasswordEncoder passwordEncoder;
	private final AffiliatePlatformService affiliatePlatformService;

	public AffiliateApplicationServiceImpl(AffiliateApplicationRepository affiliateApplicationRepo,
			AffiliateService affiliateService, Co2nsensusInterface co2nsensusInterface, PasswordEncoder passwordEncoder,
			AffiliatePlatformService affiliatePlatformService) {
		this.affiliateApplicationRepo = affiliateApplicationRepo;
		this.affiliateService = affiliateService;
		this.co2nsensusInterface = co2nsensusInterface;
		this.passwordEncoder = passwordEncoder;
		this.affiliatePlatformService = affiliatePlatformService;
	}

	@Override
	public AffiliateApplicationModel createApplication(AffiliateApplicationRequest request) {
		log.info("AffiliateApplicationServiceImpl.createApplication started");
		checkReferralCode(request.getAffiliateApplicationModel().getReferralCode());
		AffiliateApplication affiliateApplication = new AffiliateApplication();
		BeanUtils.copyProperties(request.getAffiliateApplicationModel(), affiliateApplication);

		affiliateApplication.setCity(new City(request.getAffiliateApplicationModel().getCity().getId()));
		affiliateApplication.setAffliatePlatforms(convert(request, affiliateApplication));
		affiliateApplication.setStatus(ApplicationStatus.PENDING);
		affiliateApplication.setPassword(passwordEncoder.encode(request.getAffiliateApplicationModel().getPassword()));
		affiliateApplication.setReferralCode(request.getAffiliateApplicationModel().getReferralCode());
		affiliateApplication = affiliateApplicationRepo.save(affiliateApplication);

		log.info("AffiliateApplicationServiceImpl.createApplication finished");
		request.getAffiliateApplicationModel().setId(affiliateApplication.getId());
		return request.getAffiliateApplicationModel();
	}

	@Override
	public AffiliateApplicationModel updateApplication(AffiliateApplicationRequest request) {
		log.info("AffiliateApplicationServiceImpl.updateApplication started");

		AffiliateApplication application = affiliateApplicationRepo
				.findById(Long.valueOf(request.getAffiliateApplicationModel().getId()))
				.orElseThrow(() -> new AffiliateNotFoundException(AffiliateErrorCodes.AFFILIATE_NOT_FOUND.getCode(),
						AffiliateErrorCodes.AFFILIATE_NOT_FOUND.getMessage()));
		if (request.getAffiliateApplicationModel().getStatus() == ApplicationStatus.SUCCESS) {
			CreateChannelRequest createChannelRequest = CreateChannelRequest.builder().code("co2missiont2")
					.name(request.getAffiliateApplicationModel().getName() + " "
							+ request.getAffiliateApplicationModel().getSurname())
					.contactEmail(application.getEmail()).isEnabled(Boolean.TRUE).build();
			CreateChannelResponse response = co2nsensusInterface.createChannel(createChannelRequest);
			affiliateService.createAffiliate(application, response.getId());
		}
		application.setStatus(request.getAffiliateApplicationModel().getStatus());
		// notify based on status

		application = affiliateApplicationRepo.save(application);
		AffiliateApplicationModel applicationModel = convert(application);
		log.info("AffiliateApplicationServiceImpl.updateApplication finished");
		return applicationModel;
	}

	@Override
	public AffiliateApplicationModel getApplication(String id) {
		log.info("AffiliateApplicationServiceImpl.getApplication started");
		AffiliateApplication affiliateApplication = affiliateApplicationRepo.findById(Long.parseLong(id)).stream()
				.findAny().orElseThrow(() -> new AffiliateNotFoundException(AffiliateErrorCodes.AFFILIATE_NOT_FOUND.getCode(),
						AffiliateErrorCodes.AFFILIATE_NOT_FOUND.getMessage()));
		AffiliateApplicationModel response = convert(affiliateApplication);
		log.info("AffiliateApplicationServiceImpl.getApplication finished");
		return response;
	}

	@Override
	public AffiliateApplicationModelList getApplications() {
		log.info("AffiliateApplicationServiceImpl.getApplications started");
		List<AffiliateApplicationModel> applicationModels = new ArrayList<>();
		List<AffiliateApplication> affiliateApplicationList = affiliateApplicationRepo.findAll();
		affiliateApplicationList.forEach(app -> applicationModels.add(convert(app)));
		AffiliateApplicationModelList modelList = AffiliateApplicationModelList.builder()
				.affiliateApplicationModels(applicationModels).build();
		log.info("AffiliateApplicationServiceImpl.getApplications finished");
		return modelList;
	}

	@Override
	public String deleteApplication(String id) {
		log.info("AffiliateApplicationServiceImpl.deleteApplication started");

		affiliateApplicationRepo.deleteById(Long.valueOf(id));

		log.info("AffiliateApplicationServiceImpl.deleteApplication finished");

		return "success";
	}

	private List<AffiliatePlatform> convert(AffiliateApplicationRequest request,
			AffiliateApplication affiliateApplication) {
		List<AffiliatePlatform> platforms = new ArrayList<>();
		request.getAffiliateApplicationModel().getPlatforms().stream()
				.forEach(p -> platforms.add(
						AffiliatePlatform.builder().affiliateApplication(affiliateApplication).detail(p.getDetail())
								.platform(new Platform(UUID.fromString(p.getPlatform().getId()))).build()));
		return platforms;
	}

//	private List<Platform> convert(AffiliateApplicationRequest request, AffiliateApplication affiliateApplication) {
//		List<Platform> platforms = new ArrayList<>();
//		request.getAffiliateApplicationModel().getPlatforms().stream()
//				.forEach(p -> platforms.add(new Platform(p.getUrl(),
//						affiliatePlatformService.getAffiliatePlatformType(p.getUrl()), affiliateApplication)));
//		return platforms;
//	}

	@Override
	public AffiliateApplicationModelList getApplicationsPaged(Pageable requestedPage) {
		log.info("AffiliateApplicationModelList.getApplicationsPaged started");

		Page<AffiliateApplication> affiliateApplicationsPaged = affiliateApplicationRepo.findAll(requestedPage);
		List<AffiliateApplication> affiliateApplications = affiliateApplicationsPaged.getContent();
		List<AffiliateApplicationModel> applicationModels = new ArrayList<>();

		affiliateApplications.forEach(app -> applicationModels.add(convert(app)));
		AffiliateApplicationModelList modelList = AffiliateApplicationModelList.builder()
				.affiliateApplicationModels(applicationModels).build();

		log.info("AffiliateApplicationModelList.getApplicationsPaged finished");

		return modelList;
	}

	private AffiliateApplicationModel convert(AffiliateApplication application) {
		return AffiliateApplicationModel.builder()
				.city(new CityModel(application.getCity().getId(), application.getCity().getName(),
						application.getCity().getCode()))
				.companyName(application.getCompanyName())
				.country(new CountryModel(application.getCity().getCountry().getId(),
						application.getCity().getCountry().getName()))
				.email(application.getEmail()).id(application.getId()).name(application.getName())
				.state(application.getState()).status(application.getStatus()).street(application.getStreet())
				.platforms(application.getAffliatePlatforms().stream()
						.map(p -> new AffiliatePlatformModel(p.getId().toString(),
								new PlatformModel(p.getPlatform().getId().toString(), p.getPlatform().getName(),
										p.getPlatform().getUrl()),
								p.getDetail()))
						.collect(Collectors.toList()))
				.status(application.getStatus()).surname(application.getSurname()).build();
	}

	private void checkReferralCode(String referralCode) {
		if (StringUtils.isAllBlank(referralCode))
			return;
		affiliateService.getAffiliateByReferralCode(referralCode);
	}

}
