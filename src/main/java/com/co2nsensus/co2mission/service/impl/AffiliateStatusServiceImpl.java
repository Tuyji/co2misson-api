package com.co2nsensus.co2mission.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.co2nsensus.co2mission.exception.AffiliateErrorCodes;
import org.springframework.stereotype.Service;

import com.co2nsensus.co2mission.exception.AffiliateNotFoundException;
import com.co2nsensus.co2mission.model.entity.Affiliate;
import com.co2nsensus.co2mission.model.entity.AffiliateStatus;
import com.co2nsensus.co2mission.model.enums.AffiliateStatusType;
import com.co2nsensus.co2mission.repo.AffiliateRepository;
import com.co2nsensus.co2mission.repo.AffiliateStatusRepository;
import com.co2nsensus.co2mission.service.AffiliateStatusService;

@Service
public class AffiliateStatusServiceImpl implements AffiliateStatusService {

	private final AffiliateRepository affiliateRepository;
	private final AffiliateStatusRepository affiliateStatusRepository;

	public AffiliateStatusServiceImpl(AffiliateRepository affiliateRepository,
			AffiliateStatusRepository affiliateStatusRepository) {
		this.affiliateRepository = affiliateRepository;
		this.affiliateStatusRepository = affiliateStatusRepository;
	}

	@Override
	public AffiliateStatus getAffiliateMonthStatus(UUID affiliateId, String year, String month) {
		Affiliate affiliate = affiliateRepository.findById(affiliateId)
				.orElseThrow(() -> new AffiliateNotFoundException(AffiliateErrorCodes.AFFILIATE_NOT_FOUND.getCode(),
						AffiliateErrorCodes.AFFILIATE_NOT_FOUND.getMessage() + " id: " + affiliateId.toString()));
		return null;
	}

	@Override
	public AffiliateStatus getAffiliateCurrentStatus(UUID affiliateId, String year, String month) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AffiliateStatus> getAffiliateYearlyStatus(UUID affiliateId, String year) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AffiliateStatus getAffiliateStatus(BigDecimal monthlyTotal) {
		List<AffiliateStatus> statuses = affiliateStatusRepository.findAll();
		return statuses.stream()
				.filter(st -> st.isActive() && st.getLowerLimit().compareTo(monthlyTotal) <= 0
						&& (st.getUpperLimit() == null || st.getUpperLimit().compareTo(monthlyTotal) == 1))
				.findFirst().orElseThrow();
	}
	
	@Override
	public AffiliateStatus getAffiliateStatus(AffiliateStatusType type) {
		return affiliateStatusRepository.findByAffiliateStatusType(type);
	}

}
