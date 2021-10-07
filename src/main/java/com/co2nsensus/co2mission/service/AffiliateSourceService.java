package com.co2nsensus.co2mission.service;

import com.co2nsensus.co2mission.model.enums.SourceType;
import com.co2nsensus.co2mission.model.response.source.AffiliateSourceModel;
import com.co2nsensus.co2mission.model.response.source.AffiliateSourceModelList;

public interface AffiliateSourceService {

	public AffiliateSourceModel addSource(String affiliateId, AffiliateSourceModel request);

	public AffiliateSourceModelList getAffiliateSources(String affiliateId, SourceType sourceType);
	
	public AffiliateSourceModelList getAllAffiliateSources(String affiliateId);
}
