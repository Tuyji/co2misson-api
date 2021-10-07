package com.co2nsensus.co2mission.model.response.platform;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AffiliatePlatformModelList {

	private List<AffiliatePlatformModel> affiliatePlatformModels;

}
