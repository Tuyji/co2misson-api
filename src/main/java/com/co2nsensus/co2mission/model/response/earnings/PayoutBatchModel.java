package com.co2nsensus.co2mission.model.response.earnings;

import java.math.BigDecimal;

import com.co2nsensus.co2mission.model.enums.PayoutBatchStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayoutBatchModel {
	private String id;
	private String batchSendDate;
	private String batchCompleteDate;
	private BigDecimal totalPayout;
	private int totalPayouts;
	private PayoutBatchStatus payoutBatchStatus;
	
}
