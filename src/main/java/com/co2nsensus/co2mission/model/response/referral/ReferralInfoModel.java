package com.co2nsensus.co2mission.model.response.referral;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReferralInfoModel {
	private BigDecimal turnover;
	private BigDecimal pendingPayout;
	private BigDecimal totalPayout;
	private String affiliateName;
}
