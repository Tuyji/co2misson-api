package com.co2nsensus.co2mission.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.co2nsensus.co2mission.auth.AuthenticationFacade;
import com.co2nsensus.co2mission.exception.AffiliateErrorCodes;
import com.co2nsensus.co2mission.exception.AffiliateNotFoundException;
import com.co2nsensus.co2mission.exception.AffiliateReferralNotFoundException;
import com.co2nsensus.co2mission.mapper.AffiliateMapper;
import com.co2nsensus.co2mission.model.dto.AffiliateEntityListPagedModel;
import com.co2nsensus.co2mission.model.entity.Affiliate;
import com.co2nsensus.co2mission.model.entity.AffiliateApplication;
import com.co2nsensus.co2mission.model.entity.platform.AffiliatePlatform;
import com.co2nsensus.co2mission.model.enums.AffiliateVerificationStatus;
import com.co2nsensus.co2mission.model.enums.PaymentVerificationStatus;
import com.co2nsensus.co2mission.model.enums.SourceType;
import com.co2nsensus.co2mission.model.response.affiliate.AffiliateModel;
import com.co2nsensus.co2mission.model.response.affiliate.AffiliateModelList;
import com.co2nsensus.co2mission.model.response.affiliate.AffiliateResponse;
import com.co2nsensus.co2mission.repo.AffiliateRepository;
import com.co2nsensus.co2mission.repo.platform.AffiliatePlatformRepository;
import com.co2nsensus.co2mission.service.AffiliateService;
import com.co2nsensus.co2mission.service.RedisService;
import com.co2nsensus.co2mission.utils.ListUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AffiliateServiceImpl implements AffiliateService {

	private final AffiliateRepository affiliateRepository;

	private final AffiliatePlatformRepository platformRepository;

	private final AuthenticationFacade authentication;

	private final RedisService redisService;

	private final AffiliateMapper affiliateMapper;

	public AffiliateServiceImpl(AffiliateRepository affiliateRepository, AffiliatePlatformRepository platformRepository,
			AuthenticationFacade authentication, RedisService redisService, AffiliateMapper affiliateMapper) {
		this.affiliateRepository = affiliateRepository;
		this.platformRepository = platformRepository;
		this.authentication = authentication;
		this.redisService = redisService;
		this.affiliateMapper = affiliateMapper;
	}

	@Value("${reset.password.token.expiration}")
	private Long resetPasswordTokenExpiration;

	@Override
	public AffiliateModelList getAffiliates() {
		List<Affiliate> affiliates = affiliateRepository.findAll();

		ListUtils<AffiliateModel> utils = BeanUtils.instantiate(ListUtils.class);
		List<AffiliateModel> affiliateModels = new ArrayList<>();
		utils.copyList(affiliates, affiliateModels, AffiliateModel.class);

		return AffiliateModelList.builder().affiliateModels(affiliateModels).build();
	}

	@Override
	public AffiliateResponse getAffiliatesPaged(String nameFilter, Pageable requestedPage) {
//		Page<Affiliate> affiliatePages = affiliateRepository
//				.findAllByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(nameFilter, nameFilter,
//						requestedPage);
//		List<AffiliateModel> affiliateModels = affiliatePages.getContent().stream().map(a -> affiliateMapper.convert(a))
//				.collect(Collectors.toList());
//		return AffiliateResponse.builder().affiliateModels(affiliateModels).totalPages(affiliatePages.getTotalPages())
//				.pageNumber(affiliatePages.getNumber()).pageSize(affiliatePages.getSize()).build();
		return null;
	}

	@Override
	public AffiliateModel getAffiliateById(String id) {
		log.info("AffiliateServiceImpl.getAffiliateById started");

		Affiliate affiliate = affiliateRepository.findById(UUID.fromString(id)).stream().findAny()
				.orElseThrow(() -> new AffiliateNotFoundException(AffiliateErrorCodes.AFFILIATE_NOT_FOUND.getCode(),
						AffiliateErrorCodes.AFFILIATE_NOT_FOUND.getMessage()));

		AffiliateModel affiliateModel = affiliateMapper.convert(affiliate);
		affiliateModel.setNewTransactionCount(redisService.getUnseenTransactionCount(id));
		log.info("AffiliateServiceImpl.getAffiliateById finished");

		return affiliateModel;
	}

	@Override
	public Affiliate getAffiliateByChannelId(String channelId) {
		Affiliate affiliate = affiliateRepository.findByChannelId(channelId).stream().findAny()
				.orElseThrow(() -> new AffiliateNotFoundException(AffiliateErrorCodes.AFFILIATE_NOT_FOUND.getCode(),
						AffiliateErrorCodes.AFFILIATE_NOT_FOUND.getMessage()));
		return affiliate;
	}

	@Override
	public Affiliate createAffiliate(AffiliateApplication application, String channelId) {
		Affiliate affiliate = new Affiliate();
		affiliate = Affiliate.builder().active(true).channelId(channelId).city(application.getCity())
				.companyName(application.getCompanyName()).email(application.getEmail())
				.firstName(application.getName()).lastName(application.getSurname()).password(application.getPassword())
				.phone(application.getPhone()).postcode(application.getPostcode()).state(application.getState())
				.paymentVerificationStatus(PaymentVerificationStatus.NOT_VERIFIED)
				.referrerCode(UUID.randomUUID().toString()).createdAt(LocalDateTime.now())
				.verificationStatus(AffiliateVerificationStatus.NOT_VERIFIED).build();
		if (StringUtils.isNoneBlank(application.getReferralCode())) {
			Affiliate referrer = affiliateRepository.findByReferrerCode(application.getReferralCode())
					.orElseThrow(() -> new AffiliateNotFoundException(AffiliateErrorCodes.AFFILIATE_NOT_FOUND.getCode(),
							AffiliateErrorCodes.AFFILIATE_NOT_FOUND.getMessage() + " Referreal Code is not valid: "
									+ application.getReferralCode()));
			referrer.getReferrals().add(affiliate);
			affiliate.setReferrer(referrer);
		}
		Affiliate affiliateSaved = affiliateRepository.save(affiliate);
		if (!application.getAffliatePlatforms().isEmpty()) {
			for (AffiliatePlatform platform : application.getAffliatePlatforms()) {
				platform.setAffiliate(affiliateSaved);
				platformRepository.save(platform);
			}
		}
		return affiliateSaved;
	}

	@Override
	public AffiliateModel updateAffiliate(String id, AffiliateModel affiliateModel) {
		Affiliate affiliate = affiliateRepository.findById(UUID.fromString(id))
				.orElseThrow(() -> new AffiliateNotFoundException(AffiliateErrorCodes.AFFILIATE_NOT_FOUND.getCode(),
						AffiliateErrorCodes.AFFILIATE_NOT_FOUND.getMessage() + " id: " + affiliateModel.getId()));
		affiliate.setFirstName(affiliateModel.getFirstName());
//		BeanUtils.copyProperties(affiliateModel, affiliate);
		affiliateRepository.save(affiliate);
		return affiliateMapper.convert(affiliate);
	}
	
	@Override
	public void updateAffiliateStatus(String id, AffiliateVerificationStatus status) {
		Affiliate affiliate = getAffiliateEntityById(id);
		affiliate.setVerificationStatus(status);
		affiliateRepository.save(affiliate);
	}

	@Override
	public Affiliate getAffiliateByReferralCode(String referralCode) {
		return affiliateRepository.findByReferrerCode(referralCode)
				.orElseThrow(() -> new AffiliateReferralNotFoundException(
						AffiliateErrorCodes.AFFILIATE_REFERRAL_NOT_FOUND.getCode(),
						AffiliateErrorCodes.AFFILIATE_REFERRAL_NOT_FOUND.getMessage()));
	}

	@Override
	@Transactional
	public Affiliate getAffiliateEntityById(String id) {
		return affiliateRepository.findById(UUID.fromString(id)).stream().findAny()
				.orElseThrow(() -> new AffiliateNotFoundException(AffiliateErrorCodes.AFFILIATE_NOT_FOUND.getCode(),
						AffiliateErrorCodes.AFFILIATE_NOT_FOUND.getMessage()));
	}

	@Override
	public AffiliateModel getAffiliate() {
		Affiliate affiliate = affiliateRepository.findById(UUID.fromString(authentication.getUserPrincipal().getId()))
				.stream().findAny()
				.orElseThrow(() -> new AffiliateNotFoundException(AffiliateErrorCodes.AFFILIATE_NOT_FOUND.getCode(),
						AffiliateErrorCodes.AFFILIATE_NOT_FOUND.getMessage()));

		AffiliateModel affiliateModel = affiliateMapper.convert(affiliate);
		affiliateModel.setNewTransactionCount(
				redisService.getUnseenTransactionCount(authentication.getUserPrincipal().getId()));

		return affiliateModel;
	}

	@Override
	@Transactional
	public Affiliate saveAffiliate(Affiliate affiliate) {
		return affiliateRepository.save(affiliate);
	}

	@Override
	public AffiliateEntityListPagedModel getAffiliateEntitiesPaged(String nameFilter, SourceType sourceType,
			AffiliateVerificationStatus verificationStatus, PaymentVerificationStatus paymentVerificationStatus,
			String platformId, Pageable requestedPage) {
		Page<Affiliate> affiliatesPage;
		boolean checkVerificatonStatus = verificationStatus != null;
		boolean checkPaymentVerificationStatus = paymentVerificationStatus != null;
		if (sourceType != null) {
			affiliatesPage = affiliateRepository.findAffiliatesPagedBySourceType(nameFilter, sourceType,
					verificationStatus, checkVerificatonStatus, paymentVerificationStatus,
					checkPaymentVerificationStatus, requestedPage);
		} else if (StringUtils.isNoneBlank(platformId)) {
			affiliatesPage = affiliateRepository.findAffiliatesPagedByPlatform(nameFilter, UUID.fromString(platformId),
					verificationStatus, checkVerificatonStatus, paymentVerificationStatus,
					checkPaymentVerificationStatus, requestedPage);
		} else {
			affiliatesPage = affiliateRepository.findAffiliatesPaged(nameFilter, verificationStatus,
					checkVerificatonStatus, paymentVerificationStatus, checkPaymentVerificationStatus, requestedPage);
		}

		return AffiliateEntityListPagedModel.builder().affiliateList(affiliatesPage.getContent())
				.pageNumber(affiliatesPage.getNumber()).pageSize(affiliatesPage.getSize())
				.totalPages(affiliatesPage.getTotalPages()).build();
	}

}
