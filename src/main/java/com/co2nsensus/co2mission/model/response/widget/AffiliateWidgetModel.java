package com.co2nsensus.co2mission.model.response.widget;

import com.co2nsensus.co2mission.model.enums.WidgetTheme;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AffiliateWidgetModel {
	private Long id;
	private String host;
	private WidgetTheme theme;
	private String externalId;
}
