package com.co2nsensus.co2mission.model.entity.platform;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.co2nsensus.co2mission.model.entity.Affiliate;
import com.co2nsensus.co2mission.model.entity.AffiliateApplication;
import com.co2nsensus.co2mission.model.entity.EntityWithUUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "T_AFFILIATE_PLATFORM")
public class AffiliatePlatform extends EntityWithUUID{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8835163772904558949L;

	public AffiliatePlatform(UUID id) {
		this.id = id;
	}
	
	@ManyToOne
	@JoinColumn(name="platform_id")
	private Platform platform;
	private String detail;
	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "affiliate_id")
	private Affiliate affiliate;

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "application_id")
	private AffiliateApplication affiliateApplication;
}
