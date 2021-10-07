package com.co2nsensus.co2mission.model.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.co2nsensus.co2mission.model.enums.PayoutBatchStatus;

import lombok.Data;

@Data
@Entity
@Table(name="T_PAYOUT_BATCH")
public class PayoutBatch extends EntityWithUUID{/**
	 * 
	 */
	private static final long serialVersionUID = -550184816381013631L;

	@OneToMany(mappedBy = "payoutBatch", cascade = CascadeType.ALL,orphanRemoval=true)
	private List<PayoutTransaction> payoutTransactions;
	private String startedBy;
	private PayoutBatchStatus status;
	private LocalDateTime createdAt;
	private LocalDateTime completedAt;
	private String batchJobId;
}
