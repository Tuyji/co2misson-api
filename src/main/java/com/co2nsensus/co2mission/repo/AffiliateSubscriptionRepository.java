package com.co2nsensus.co2mission.repo;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.co2nsensus.co2mission.model.entity.AffiliateSubscription;

@Repository
public interface AffiliateSubscriptionRepository extends JpaRepository<AffiliateSubscription, UUID> {
	Optional<AffiliateSubscription> findBySubscriptionId(String subscriptionId);
}
