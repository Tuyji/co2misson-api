package com.co2nsensus.co2mission.model.entity;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.co2nsensus.co2mission.model.enums.AffiliateStatusType;

import lombok.Data;

@Data
@Entity
@Table(name="T_AFFILIATE_STATUS")
public class AffiliateStatus extends EntityWithUUID{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5179976321530280915L;
	private BigDecimal lowerLimit;
	private BigDecimal upperLimit;
	private AffiliateStatusType affiliateStatusType;
	private BigDecimal comissionRate;
	private boolean isActive;
}
