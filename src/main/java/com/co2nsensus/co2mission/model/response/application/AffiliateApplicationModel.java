package com.co2nsensus.co2mission.model.response.application;

import java.util.List;

import com.co2nsensus.co2mission.model.enums.ApplicationStatus;
import com.co2nsensus.co2mission.model.response.location.CityModel;
import com.co2nsensus.co2mission.model.response.location.CountryModel;
import com.co2nsensus.co2mission.model.response.platform.AffiliatePlatformModel;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class AffiliateApplicationModel {

	private Long id;
	private String name;
	private String surname;
	private String password;
	private String phone;
	private String companyName;
	private String street;
	private CountryModel country;
	private CityModel city;
	private String postcode;
	private String state;
	private String email;
	private ApplicationStatus status;
	private List<AffiliatePlatformModel> platforms;
	private String referralCode;

}
