package com.co2nsensus.co2mission.model.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "T_SYSTEM_PARAMETER")
public class SystemParameter extends EntityWithUUID{
	/**
	 * 
	 */
	private static final long serialVersionUID = -234266764744855005L;
	private String name; 
	private String value;
}
