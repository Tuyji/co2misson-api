package com.co2nsensus.co2mission.model.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "T_OFFSET_PROJECT")
public class OffsetProject extends EntityWithUUID{/**
	 * 
	 */
	private static final long serialVersionUID = 6601289865054572021L;
	
	private String externalId;
	private String projectName;
	
}
