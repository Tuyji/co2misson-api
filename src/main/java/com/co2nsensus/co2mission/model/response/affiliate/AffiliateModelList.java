package com.co2nsensus.co2mission.model.response.affiliate;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AffiliateModelList {

	private List<AffiliateModel> affiliateModels;

}
