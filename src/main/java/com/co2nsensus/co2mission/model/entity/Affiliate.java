package com.co2nsensus.co2mission.model.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.co2nsensus.co2mission.model.entity.platform.AffiliatePlatform;
import com.co2nsensus.co2mission.model.entity.source.AffiliateSource;
import com.co2nsensus.co2mission.model.enums.AffiliateVerificationStatus;
import com.co2nsensus.co2mission.model.enums.PaymentVerificationStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "T_AFFILIATE")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Affiliate extends EntityWithUUID {

	/**
	 * 
	 */
	private static final long serialVersionUID = 276607710412389996L;

	public Affiliate(String id) {
		this.id = UUID.fromString(id);
	}

	@Column(nullable = false)
	private String firstName;
	@Column(nullable = false)
	private String lastName;
	@Column(nullable = false)
	private String password;
	@Column(nullable = false)
	private String email;
	@Column(nullable = false)
	private boolean active;
	private LocalDateTime createdAt;
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "affiliate")
	private Set<AffiliateSource> affiliateSources;
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "affiliate",cascade = CascadeType.ALL)
	private Set<AffiliateWidget> affiliateWidgets;
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "affiliate",cascade = CascadeType.ALL)
	private List<AffiliatePlatform> affiliatePlatforms;
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "affiliate")
	private Set<PaymentTransaction> paymentTransactions;
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "affiliate")
	private Set<PayoutTransaction> payoutTransactions;
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "affiliate")
	private Set<AffiliatePaymentInformation> paymentInformations;
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "affiliate")
	private Set<AffiliateSubscription> subscriptions;
	private String channelId;
	private String phone;
	private String companyName;
	private String postcode;
	private String state;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "city_id")
	private City city;
	private AffiliateVerificationStatus verificationStatus;
	private PaymentVerificationStatus paymentVerificationStatus;
	private String referrerCode;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "referrer_id")
	private Affiliate referrer;
	@Builder.Default
	@OneToMany(mappedBy = "referrer")
	private Set<Affiliate> referrals = new HashSet<>();
	@OneToMany(mappedBy = "affiliate",cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	private List<VerificationFile> verificationFiles;

	@OneToMany(mappedBy = "affiliate", cascade = CascadeType.ALL,orphanRemoval=true)
	private List<UserRole> roles;
	
}
