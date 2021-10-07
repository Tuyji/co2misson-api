package com.co2nsensus.co2mission.model.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="T_COUNTRY")
public class Country implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7449744584503942702L;

	public Country() {
		super();
	}
	
	public Country(Long id) {
		super();
		this.id = id;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	
	@Column(name = "iso_code_2")
	private String isoCode2;
	
	@Column(name = "iso_code_3")
	private String isoCode3;
	
	@Column(name = "address_format")
	private String addressFormat;
	
	@Column(name = "post_code_required")
	private String postCodeRequired;
	
	private int status;

	
	
}
