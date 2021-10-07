package com.co2nsensus.co2mission.model.entity.source;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.co2nsensus.co2mission.model.entity.Affiliate;
import com.co2nsensus.co2mission.model.entity.AffiliateWidget;
import com.co2nsensus.co2mission.model.entity.EntityWithUUID;
import com.co2nsensus.co2mission.model.entity.platform.AffiliatePlatform;
import com.co2nsensus.co2mission.model.enums.SourceType;

import lombok.Data;

@Entity
@Table(name = "T_AFFILIATE_SOURCE")
@Data
public class AffiliateSource extends EntityWithUUID {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6222621415767720717L;
	private String name;
	private String analyticsName;
	@ManyToOne
	@JoinColumn(name = "affiliate_id")
	private Affiliate affiliate;
	@Enumerated(EnumType.STRING)
	private SourceType type;
	@ManyToOne(optional = true)
	@JoinColumn(name = "platform_id")
	private AffiliatePlatform platform;
	@OneToOne(optional = true)
	@JoinColumn(name = "widget_id", referencedColumnName = "id")
	private AffiliateWidget widget;
}
