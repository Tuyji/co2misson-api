package com.co2nsensus.co2mission.repo;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.co2nsensus.co2mission.model.entity.PayoutTransaction;

public interface PayoutTransactionRepository extends JpaRepository<PayoutTransaction, UUID>{
	List<PayoutTransaction> findAllByAffiliateIdOrderByCreatedAtDesc(UUID affiliateId);
}
