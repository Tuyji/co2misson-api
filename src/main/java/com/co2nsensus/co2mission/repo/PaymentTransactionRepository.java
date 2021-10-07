package com.co2nsensus.co2mission.repo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.co2nsensus.co2mission.model.entity.PaymentTransaction;
import com.co2nsensus.co2mission.model.enums.TransactionType;

@Repository
public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, UUID> {

	List<PaymentTransaction> findAllByAffiliateIdAndTransactionSourceAndCreatedAtBetweenAndTransactionType(
			UUID affiliateId, String source, LocalDateTime startDate, LocalDateTime endDate,
			TransactionType transactionType);

	List<PaymentTransaction> findAllByAffiliateIdAndCreatedAtBetweenAndTransactionType(UUID affiliateId,
			LocalDateTime startDate, LocalDateTime endDate, TransactionType transactionType);

	List<PaymentTransaction> findAllByAffiliateIdAndCreatedAtBetween(UUID affiliateId, LocalDateTime startDate,
			LocalDateTime endDate);

	List<PaymentTransaction> findAllByAffiliateId(UUID affiliateId);

	Page<PaymentTransaction> findAllByAffiliateId(UUID affiliateId, Pageable pageable);
}
