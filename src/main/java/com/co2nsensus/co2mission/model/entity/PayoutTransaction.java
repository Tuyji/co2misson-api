package com.co2nsensus.co2mission.model.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "T_PAYOUT_TRANSACTION")
public class PayoutTransaction extends EntityWithUUID {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3678996604840810714L;
	private BigDecimal amount;
	private String currency;
	private LocalDateTime createdAt;
	private LocalDateTime completedAt;
	@ManyToOne(optional = false)
	@JoinColumn(name = "affiliate_id", referencedColumnName = "id")
	private Affiliate affiliate;
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "payoutTransaction", cascade = CascadeType.ALL)
	private List<PaymentTransaction> paymentTransactions;
	private String startedBy;
	@ManyToOne
	@JoinColumn(name = "payout_batch_id", referencedColumnName = "id")
	private PayoutBatch payoutBatch;
	private LocalDate earningStart;
	private LocalDate earningEnd;
}
