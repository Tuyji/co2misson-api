package com.co2nsensus.co2mission.repo;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.co2nsensus.co2mission.model.entity.Affiliate;
import com.co2nsensus.co2mission.model.enums.AffiliateVerificationStatus;
import com.co2nsensus.co2mission.model.enums.PaymentVerificationStatus;
import com.co2nsensus.co2mission.model.enums.SourceType;

@Repository
public interface AffiliateRepository extends JpaRepository<Affiliate, UUID> {

	Optional<Affiliate> findByEmail(String email);

	Optional<Affiliate> findByChannelId(String channelId);

	Optional<Affiliate> findByReferrerCode(String referrerCode);

	Page<Affiliate> findAllByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseAndAffiliatePlatformsPlatformName(
			String nameFilter, String surnameFilter, String platformName, Pageable pageable);

	@Query("select a from Affiliate a join a.affiliatePlatforms p where (lower(a.firstName) like lower(concat('%', :nameFilter,'%')) or lower(a.lastName) like lower(concat('%', :nameFilter,'%')))"
			+ " and p.platform.id = :platformId and (a.verificationStatus = :verificationStatus or :checkVerificationStatus = false) and (a.paymentVerificationStatus = :paymentVerificationStatus or :checkPaymentVerificationStatus = false)")
	Page<Affiliate> findAffiliatesPagedByPlatform(String nameFilter, UUID platformId,
			AffiliateVerificationStatus verificationStatus, boolean checkVerificationStatus,
			PaymentVerificationStatus paymentVerificationStatus, boolean checkPaymentVerificationStatus,
			Pageable pageable);

	@Query("select a from Affiliate a join a.affiliateSources s where (lower(a.firstName) like lower(concat('%', :nameFilter,'%')) or lower(a.lastName) like lower(concat('%', :nameFilter,'%')))  and s.type = :sourceType and (a.verificationStatus = :verificationStatus or :checkVerificationStatus = false) and (a.paymentVerificationStatus = :paymentVerificationStatus or :checkPaymentVerificationStatus = false)")
	Page<Affiliate> findAffiliatesPagedBySourceType(String nameFilter, SourceType sourceType,
			AffiliateVerificationStatus verificationStatus, boolean checkVerificationStatus,
			PaymentVerificationStatus paymentVerificationStatus, boolean checkPaymentVerificationStatus,
			Pageable pageable);

	@Query("select a from Affiliate a where lower(a.firstName) like lower(concat('%', :nameFilter,'%')) or lower(a.lastName) like lower(concat('%', :nameFilter,'%')) and (a.verificationStatus = :verificationStatus or :checkVerificationStatus = false) and (a.paymentVerificationStatus = :paymentVerificationStatus or :checkPaymentVerificationStatus = false)")
	Page<Affiliate> findAffiliatesPaged(String nameFilter, AffiliateVerificationStatus verificationStatus,
			boolean checkVerificationStatus, PaymentVerificationStatus paymentVerificationStatus,
			boolean checkPaymentVerificationStatus, Pageable pageable);

}
