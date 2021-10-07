package com.co2nsensus.co2mission.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyStatusModel {
	private String statusName;
	private BigDecimal lowerLimit;
	private BigDecimal upperLimit;
	private BigDecimal comissionRate;
}
