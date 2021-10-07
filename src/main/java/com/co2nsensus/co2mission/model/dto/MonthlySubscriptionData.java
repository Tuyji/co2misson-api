package com.co2nsensus.co2mission.model.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonthlySubscriptionData {
	@Builder.Default
	private BigDecimal totalSales = new BigDecimal(0);
	private int treesPlanted;
	@Builder.Default
	private BigDecimal totalOffset = new BigDecimal(0);

}
