package com.co2nsensus.co2mission.model.response.transaction;

import com.co2nsensus.co2mission.model.response.affiliate.AffiliateModel;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AffiliateTransactionMetrics {
	private TransactionHistoryResponseModel transactionHistory;
	private AffiliateModel affiliate;
}
