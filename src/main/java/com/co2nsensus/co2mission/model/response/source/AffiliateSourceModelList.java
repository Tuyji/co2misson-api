package com.co2nsensus.co2mission.model.response.source;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AffiliateSourceModelList {
	@Default
	private List<AffiliateSourceModel> sourceList = new ArrayList<>();
}
