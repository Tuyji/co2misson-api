package com.co2nsensus.co2mission.model.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "T_USER_ROLE")
public class UserRole{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	public UserRole() {
		super();
	}

	public UserRole(Affiliate affiliate, Role role) {
		super();
		this.affiliate = affiliate;
		this.role = role;
	}
	
	public UserRole(Admin admin, Role role) {
		super();
		this.admin = admin;
		this.role = role;
	}

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "affiliate_id")
	private Affiliate affiliate;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "admin_id")
	private Admin admin;
	
	@ManyToOne
	@JoinColumn(name = "role_id")
	private Role role;
	

	public Affiliate getAffiliate() {
		return affiliate;
	}

	public void setAffiliate(Affiliate affiliate) {
		this.affiliate = affiliate;
	}

	public Admin getAdmin() {
		return admin;
	}

	public void setAdmin(Admin admin) {
		this.admin = admin;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
}
