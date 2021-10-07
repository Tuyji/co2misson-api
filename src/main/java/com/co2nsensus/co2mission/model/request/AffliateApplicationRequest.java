package com.co2nsensus.co2mission.model.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AffliateApplicationRequest {
	
	private String name;
	private String surname;
	private String email;
	//status
}

