package com.co2nsensus.co2mission.model.response.transaction;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AffiliateTransactionMetricsList {
	private List<AffiliateTransactionMetrics> affiliateList;
	private int totalPages;
}
