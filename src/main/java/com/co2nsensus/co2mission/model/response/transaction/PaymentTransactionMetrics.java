package com.co2nsensus.co2mission.model.response.transaction;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PaymentTransactionMetrics {

    private int totalClicks;
    private BigDecimal minSalesAmount;
    private BigDecimal maxSalesAmount;
    private BigDecimal averageSalesAmount;
    private int numberOfOffsets;
    private BigDecimal totalOffsetAmount;
    private BigDecimal totalSalesAmount;
    private BigDecimal myEarnings;

}
