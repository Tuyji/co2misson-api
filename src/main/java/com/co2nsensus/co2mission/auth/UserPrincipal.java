package com.co2nsensus.co2mission.auth;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.co2nsensus.co2mission.model.entity.Admin;
import com.co2nsensus.co2mission.model.entity.Affiliate;
import com.co2nsensus.co2mission.model.enums.AffiliateVerificationStatus;
import com.co2nsensus.co2mission.model.enums.PaymentVerificationStatus;

public class UserPrincipal implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6915796573842978254L;

	private String id;
	private String name;
	private String surname;
	private String email;
	private String password;
	private String ip;
	private String userAgent;
	private Collection<? extends GrantedAuthority> authorities;
	private AffiliateVerificationStatus verificationStatus;
	private PaymentVerificationStatus paymentVerificationStatus;

	public UserPrincipal(String id, String companyId, Collection<? extends GrantedAuthority> authorities) {
		this.id = id;
		this.authorities = authorities;
	}

	public UserPrincipal(String id, String name, String surname, String email,
			Collection<? extends GrantedAuthority> authorities, String password,
			AffiliateVerificationStatus verificationStatus, PaymentVerificationStatus paymentVerificationStatus) {
		super();
		this.id = id;
		this.name = name;
		this.surname = surname;
		this.email = email;
		this.authorities = authorities;
		this.password = password;
		this.verificationStatus = verificationStatus;
		this.paymentVerificationStatus = paymentVerificationStatus;
	}

	public UserPrincipal(String id, String name, String surname, String email,
			Collection<? extends GrantedAuthority> authorities, String password) {
		super();
		this.id = id;
		this.name = name;
		this.surname = surname;
		this.email = email;
		this.authorities = authorities;
		this.password = password;
	}

	public static UserPrincipal create(Affiliate affiliate) {
		List<GrantedAuthority> authorities = affiliate.getRoles().stream()
				.map(role -> new SimpleGrantedAuthority(role.getRole().getRoleName())).collect(Collectors.toList());
		return new UserPrincipal(affiliate.getId().toString(), affiliate.getFirstName(), affiliate.getLastName(),
				affiliate.getEmail(), authorities, affiliate.getPassword(), affiliate.getVerificationStatus(),
				affiliate.getPaymentVerificationStatus());
	}

	public static UserPrincipal create(Admin admin) {
		List<GrantedAuthority> authorities = admin.getRoles().stream()
				.map(role -> new SimpleGrantedAuthority(role.getRole().getRoleName())).collect(Collectors.toList());
		return new UserPrincipal(admin.getId().toString(), admin.getName(), admin.getSurname(), admin.getEmail(),
				authorities, admin.getPassword());
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public String getUsername() {
		return name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public AffiliateVerificationStatus getVerificationStatus() {
		return verificationStatus;
	}

	public void setVerificationStatus(AffiliateVerificationStatus verificationStatus) {
		this.verificationStatus = verificationStatus;
	}

	public PaymentVerificationStatus getPaymentVerificationStatus() {
		return paymentVerificationStatus;
	}

	public void setPaymentVerificationStatus(PaymentVerificationStatus paymentVerificationStatus) {
		this.paymentVerificationStatus = paymentVerificationStatus;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
		this.authorities = authorities;
	}

}
