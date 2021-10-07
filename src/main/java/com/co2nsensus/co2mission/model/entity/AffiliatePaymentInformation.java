package com.co2nsensus.co2mission.model.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="T_AFFILIATE_PAYMENT_INFORMATION")
public class AffiliatePaymentInformation extends EntityWithUUID{
	/**
	 * 
	 */
	private static final long serialVersionUID = 947229150106640909L;
	@ManyToOne
	@JoinColumn(name="affiliate_id")
	private Affiliate affiliate;
	private String accountInfo;
	private boolean isActive;
	private PaymentType paymentType;
}
