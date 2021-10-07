package com.co2nsensus.co2mission.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.co2nsensus.co2mission.model.entity.AffiliateStatus;
import com.co2nsensus.co2mission.model.enums.AffiliateStatusType;

public interface AffiliateStatusService {
	AffiliateStatus getAffiliateMonthStatus(UUID affiliateId,String year,String month);
	
	AffiliateStatus getAffiliateCurrentStatus(UUID affiliateId,String year,String month);
	
	List<AffiliateStatus> getAffiliateYearlyStatus(UUID affiliateId,String year);
	
	AffiliateStatus getAffiliateStatus(BigDecimal monthlyTotal);
	
	AffiliateStatus getAffiliateStatus(AffiliateStatusType type);
}
