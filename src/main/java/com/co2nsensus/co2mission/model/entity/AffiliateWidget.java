package com.co2nsensus.co2mission.model.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.co2nsensus.co2mission.model.enums.WidgetTheme;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "T_AFFILIATE_WIDGET")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AffiliateWidget {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String externalId;
	private String host;
	private WidgetTheme theme;
	@ManyToOne
	@JoinColumn(name="affiliate_id")
	private Affiliate affiliate;
	private LocalDateTime createdAt;
}
