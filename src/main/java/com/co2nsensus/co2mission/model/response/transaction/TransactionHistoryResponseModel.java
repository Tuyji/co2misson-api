package com.co2nsensus.co2mission.model.response.transaction;

import lombok.Data;

@Data
public class TransactionHistoryResponseModel {

    private PaymentTransactionModelList paymentTransactionModels;
    private PaymentTransactionMetrics paymentTransactionMetrics;
    
}
