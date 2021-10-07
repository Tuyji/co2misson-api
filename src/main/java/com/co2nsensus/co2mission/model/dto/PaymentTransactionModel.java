package com.co2nsensus.co2mission.model.dto;

import java.math.BigDecimal;

import com.co2nsensus.co2mission.model.enums.TransactionType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentTransactionModel {

    private BigDecimal amount;
    private BigDecimal amountInDollars;
    private BigDecimal offsetAmount;
    private String currency;
    private String createdAt;
    private String channelId;
    private String orderType;
    private String name;
    private String surName;
    private TransactionType transactionType;
    private String orderId;
    private String source;
    private SubscriptionModel subscription;
    private String widgetId;
    private BigDecimal earning;
    private String projectId;
    private String projectName;
    
}
