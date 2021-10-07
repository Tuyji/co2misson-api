package com.co2nsensus.co2mission.service;

import java.util.List;

import com.co2nsensus.co2mission.model.response.earnings.PayoutBatchModel;
import com.co2nsensus.co2mission.model.response.earnings.PayoutModel;

public interface PayoutService {
	List<PayoutModel> getAffiliatePayouts(String affiliateId);
	
	List<PayoutModel> getBatchPayouts(String batchId);
	
	List<PayoutBatchModel> getBatches();
}
