package com.co2nsensus.co2mission.model.response.earnings;

import java.math.BigDecimal;

import com.co2nsensus.co2mission.model.response.affiliate.AffiliateModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayoutModel {
	private AffiliateModel affiliateModel;
	private String batchId;
	private String payoutDate;
	private String payoutCompletionDate;
	private String earningPeriod;
	private BigDecimal earnings;
	private String startedBy;
}
