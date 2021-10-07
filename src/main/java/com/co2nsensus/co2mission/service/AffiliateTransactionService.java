package com.co2nsensus.co2mission.service;

import java.time.LocalDate;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;

import com.co2nsensus.co2mission.model.enums.AffiliateVerificationStatus;
import com.co2nsensus.co2mission.model.enums.PaymentVerificationStatus;
import com.co2nsensus.co2mission.model.enums.SourceType;
import com.co2nsensus.co2mission.model.response.transaction.AffiliateTransactionMetrics;
import com.co2nsensus.co2mission.model.response.transaction.AffiliateTransactionMetricsList;
import com.co2nsensus.co2mission.model.response.transaction.PaymentTransactionModelList;
import com.co2nsensus.co2mission.model.response.transaction.TransactionHistoryResponseModel;

public interface AffiliateTransactionService {

	PaymentTransactionModelList getLastNTransactions(String affiliateId, Pageable pageable);

	TransactionHistoryResponseModel getTransactionHistory(String affiliateId, String source, LocalDate startDate,
			LocalDate endDate, int page);

	TransactionHistoryResponseModel getTransactionHistory(String affiliateId, SourceType sourceType,
			LocalDate startDate, LocalDate endDate, String projectId);

	AffiliateTransactionMetrics getTransactionMetrics(String affiliateId, SourceType sourceType,
			LocalDate startDate, LocalDate endDate, int page, String projectId, String sort, int size, String filter,
			Direction direction, AffiliateVerificationStatus verificationStatus,
			PaymentVerificationStatus paymentVerificationStatus, String platformId);
	
	AffiliateTransactionMetricsList getAllTransactionMetrics(SourceType sourceType, LocalDate startDate,
			LocalDate endDate, int page, String projectId, String sort, int size, String filter, Direction direction,
			AffiliateVerificationStatus verificationStatus, PaymentVerificationStatus paymentVerificationStatus,
			String platformId);
}
