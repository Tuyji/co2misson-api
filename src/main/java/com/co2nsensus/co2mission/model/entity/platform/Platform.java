package com.co2nsensus.co2mission.model.entity.platform;

import java.util.UUID;

/*
 * INSTAGRAM,TWITTER,CUSTOM(BLOG)
 * 
 */

import javax.persistence.Entity;
import javax.persistence.Table;

import com.co2nsensus.co2mission.model.entity.EntityWithUUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Table
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "T_PLATFORM")
public class Platform extends EntityWithUUID {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2260865752612705705L;
	
	public Platform(UUID id) {
		this.id = id;
	}
	
	private String name;
	private String url;
	private Boolean isActive;
}
