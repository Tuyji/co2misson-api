package com.co2nsensus.co2mission.service;

import org.springframework.data.domain.Pageable;

import com.co2nsensus.co2mission.model.dto.AffiliateEntityListPagedModel;
import com.co2nsensus.co2mission.model.entity.Affiliate;
import com.co2nsensus.co2mission.model.entity.AffiliateApplication;
import com.co2nsensus.co2mission.model.enums.AffiliateVerificationStatus;
import com.co2nsensus.co2mission.model.enums.PaymentVerificationStatus;
import com.co2nsensus.co2mission.model.enums.SourceType;
import com.co2nsensus.co2mission.model.response.affiliate.AffiliateModel;
import com.co2nsensus.co2mission.model.response.affiliate.AffiliateModelList;
import com.co2nsensus.co2mission.model.response.affiliate.AffiliateResponse;

public interface AffiliateService {

	AffiliateModelList getAffiliates();

	AffiliateResponse getAffiliatesPaged(String nameFilter, Pageable requestedPage);

	AffiliateEntityListPagedModel getAffiliateEntitiesPaged(String nameFilter, SourceType sourceType,
			AffiliateVerificationStatus verificationStatus, PaymentVerificationStatus paymentVerificationStatus,
			String platformName, Pageable requestedPage);

	AffiliateModel getAffiliateById(String id);

	AffiliateModel getAffiliate();

	Affiliate getAffiliateByChannelId(String channelId);

	Affiliate getAffiliateEntityById(String id);

	Affiliate getAffiliateByReferralCode(String referralCode);

	Affiliate createAffiliate(AffiliateApplication application, String channelId);

	AffiliateModel updateAffiliate(String id, AffiliateModel affiliateModel);

	Affiliate saveAffiliate(Affiliate affiliate);
	
	void updateAffiliateStatus(String id, AffiliateVerificationStatus status);

}
