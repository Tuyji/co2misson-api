package com.co2nsensus.co2mission.model.response.location;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CityModel {
	private Long id;
	private String name;
	private String code;
}
