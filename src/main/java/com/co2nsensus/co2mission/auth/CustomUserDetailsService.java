package com.co2nsensus.co2mission.auth;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.co2nsensus.co2mission.interceptor.HeaderRequestParameters;
import com.co2nsensus.co2mission.model.entity.Admin;
import com.co2nsensus.co2mission.model.entity.Affiliate;
import com.co2nsensus.co2mission.repo.AdminRepository;
import com.co2nsensus.co2mission.repo.AffiliateRepository;

@Service("CustomUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private AffiliateRepository affiliateRepository;

	@Autowired
	private AdminRepository adminRepository;

	@Autowired
	private HeaderRequestParameters headerRequestParameters;

	@Override
	@Transactional
	public UserPrincipal loadUserByUsername(String email) {
		if (headerRequestParameters.isAdmin()) {
			Optional<Admin> adminOptional = adminRepository.findByEmail(email);
			if (!adminOptional.isPresent())
				throw new UsernameNotFoundException("");

			return UserPrincipal.create(adminOptional.get());
		} else {
			Optional<Affiliate> affiliateOptional = affiliateRepository.findByEmail(email);
			if (!affiliateOptional.isPresent())
				throw new UsernameNotFoundException("");

			return UserPrincipal.create(affiliateOptional.get());
		}

	}

}