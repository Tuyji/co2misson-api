package com.co2nsensus.co2mission.model.response.earnings;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import com.co2nsensus.co2mission.model.response.MonthlyStatusModel;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyEarningsModel {
	private MonthlyStatusModel status;
	private BigDecimal totalSales;
	private BigDecimal earnings;
	private String month;
	private int year;
}
