package com.co2nsensus.co2mission.model.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="T_PAYMENT_TYPE")
public class PaymentType extends EntityWithUUID{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8784740129999092250L;
	private String name;
	private boolean isActive;
	
}
