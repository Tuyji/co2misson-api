package com.co2nsensus.co2mission.model.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.co2nsensus.co2mission.model.enums.TransactionType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@Table(name = "T_PAYMENT_TRANSACTION")
@AllArgsConstructor
@NoArgsConstructor
public class PaymentTransaction extends EntityWithUUID {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7252164141964279307L;
	private BigDecimal amount;
	private BigDecimal amountInDollars;
	private BigDecimal offsetAmount;
	private String currency;
	private LocalDateTime createdAt;
	private LocalDateTime messageTime;
	@ManyToOne(optional = true)
	@JoinColumn(name = "affiliate_id", referencedColumnName = "id")
	private Affiliate affiliate;
	@ManyToOne(optional = true)
	@JoinColumn(name = "affiliate_subscription_id", referencedColumnName = "id")
	private AffiliateSubscription affiliateSubscription;
	private String transactionSource;
	private String name;
	private String surName;
	private TransactionType transactionType;
	@Column(unique = true)
	private String orderId;
	private Boolean isBeingProcessed;
	private Boolean isPaidOut;
	private Boolean isReferrerPaidOut;
	private Boolean isReferrerBeingProcessed;
	@ManyToOne(optional = true)
	@JoinColumn(name = "payout_transaction_id", referencedColumnName = "id")
	private PayoutTransaction payoutTransaction;
	@Column(nullable = true)
	private Integer trees;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "project_id")
	private OffsetProject offsetProject;

	public static PaymentTransaction zeroPaymentTransaction() {
		return PaymentTransaction.builder().amount(new BigDecimal(0)).build();
	}
}
