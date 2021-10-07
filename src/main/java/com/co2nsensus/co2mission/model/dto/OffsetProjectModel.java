package com.co2nsensus.co2mission.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OffsetProjectModel {
	private String id;
	private String externalId;
	private String name;
}
