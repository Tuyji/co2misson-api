package com.co2nsensus.co2mission.service;

import com.co2nsensus.co2mission.model.request.AffiliateApplicationRequest;
import com.co2nsensus.co2mission.model.response.application.AffiliateApplicationModel;
import com.co2nsensus.co2mission.model.response.application.AffiliateApplicationModelList;

import org.springframework.data.domain.Pageable;

public interface AffiliateApplicationService {

	AffiliateApplicationModel createApplication(AffiliateApplicationRequest request);

	AffiliateApplicationModel updateApplication(AffiliateApplicationRequest request);

	AffiliateApplicationModel getApplication(String id);

	AffiliateApplicationModelList getApplications();

	String deleteApplication(String id);

	AffiliateApplicationModelList getApplicationsPaged(Pageable requestedPage);
}
