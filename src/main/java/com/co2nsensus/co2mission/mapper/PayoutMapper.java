package com.co2nsensus.co2mission.mapper;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

import com.co2nsensus.co2mission.model.entity.PayoutBatch;
import com.co2nsensus.co2mission.model.entity.PayoutTransaction;
import com.co2nsensus.co2mission.model.response.earnings.PayoutBatchModel;
import com.co2nsensus.co2mission.model.response.earnings.PayoutModel;

@Component
public class PayoutMapper {

	private final AffiliateMapper affiliateMapper;

	private final static DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/mm/yyyy");

	public PayoutMapper(AffiliateMapper affiliateMapper) {
		this.affiliateMapper = affiliateMapper;
	}

	public PayoutModel convert(PayoutTransaction payoutTransaction) {
		BigDecimal totalSalesAmount = payoutTransaction.getPaymentTransactions().stream()
				.map(x -> x.getAmountInDollars()).reduce(BigDecimal.ZERO, BigDecimal::add);

		return PayoutModel.builder().affiliateModel(affiliateMapper.convert(payoutTransaction.getAffiliate())).batchId(
				payoutTransaction.getPayoutBatch() != null ? payoutTransaction.getPayoutBatch().getId().toString() : "")
				.earningPeriod(DATE_FORMAT.format(payoutTransaction.getEarningStart()) + "-"
						+ DATE_FORMAT.format(payoutTransaction.getEarningEnd()))
				.payoutCompletionDate(DATE_FORMAT.format(payoutTransaction.getCompletedAt()))
				.payoutDate(DATE_FORMAT.format(payoutTransaction.getCreatedAt()))
				.startedBy(payoutTransaction.getStartedBy()).earnings(totalSalesAmount).build();
	}

	public PayoutBatchModel convert(PayoutBatch payoutBatch) {
		BigDecimal totalSalesAmount = new BigDecimal(0);
		for (PayoutTransaction payoutTransaction : payoutBatch.getPayoutTransactions()) {
			totalSalesAmount = payoutTransaction.getPaymentTransactions().stream().map(x -> x.getAmountInDollars())
					.reduce(BigDecimal.ZERO, BigDecimal::add);
		}

		return PayoutBatchModel.builder().batchCompleteDate(DATE_FORMAT.format(payoutBatch.getCompletedAt()))
				.id(payoutBatch.getId().toString()).payoutBatchStatus(payoutBatch.getStatus())
				.totalPayout(totalSalesAmount).totalPayouts(payoutBatch.getPayoutTransactions().size()).build();
	}
}
