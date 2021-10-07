package com.co2nsensus.co2mission.model.entity;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.co2nsensus.co2mission.model.enums.VerificationFileType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "T_AFFILIATE_VERIFICATION_FILE")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerificationFile extends EntityWithUUID{/**
	 * 
	 */
	private static final long serialVersionUID = 6486240059352985298L;
	private VerificationFileType fileType;
	private String fileName;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="affiliate_id")
	private Affiliate affiliate;
	@Lob
	@Basic(fetch = FetchType.LAZY)
	private byte[] file;
}
