package com.co2nsensus.co2mission.model.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="T_USEFUL_MATERIALS")
public class UsefulMaterial extends EntityWithUUID{
	/**
	 * 
	 */
	private static final long serialVersionUID = 983120523857993453L;
	private String name;
	private String content;
}
