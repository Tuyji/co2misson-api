package com.co2nsensus.co2mission.model.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.co2nsensus.co2mission.model.enums.AffiliateSubscriptionStatus;

import lombok.Data;

@Data
@Entity
@Table(name="T_AFFILIATE_SUBSCRIPTION")
public class AffiliateSubscription extends EntityWithUUID{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4305258137971494493L;

	@ManyToOne(fetch = FetchType.LAZY,optional = false)
	@JoinColumn(name="affiliate_id")
	private Affiliate affiliate;
	private String subscriptionId;
	private LocalDateTime createdAt;
	private BigDecimal monthlyPayment;
	private String monthlyPaymentCurrency;
	private BigDecimal monthlyPaymentDollar;
	private String name;
	private BigDecimal monthlyOffset;
	private int monthlyTrees;
	private boolean isActive;
	private String source;
	private AffiliateSubscriptionStatus subscriptionStatus;
	private String subscriberName;
	private String subscriberSurname;
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "affiliateSubscription",cascade = CascadeType.ALL)
	private List<PaymentTransaction> paymentTransactions;
	
}
