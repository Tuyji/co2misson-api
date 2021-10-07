package com.co2nsensus.co2mission.mapper;

import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.co2nsensus.co2mission.model.entity.Affiliate;
import com.co2nsensus.co2mission.model.response.affiliate.AffiliateModel;
import com.co2nsensus.co2mission.model.response.platform.AffiliatePlatformModel;
import com.co2nsensus.co2mission.model.response.platform.PlatformModel;

@Component
public class AffiliateMapper {

	public AffiliateModel convert(Affiliate affiliate) {
		AffiliateModel affiliateModel = new AffiliateModel();

		BeanUtils.copyProperties(affiliate, affiliateModel);
		affiliateModel.setId(affiliate.getId().toString());
		affiliateModel
				.setPlatforms(affiliate
						.getAffiliatePlatforms().stream().map(
								p -> new AffiliatePlatformModel(p.getId().toString(),
										new PlatformModel(p.getPlatform().getId().toString(), p.getPlatform().getName(),
												p.getPlatform().getUrl()),
										p.getDetail()))
						.collect(Collectors.toList()));
		affiliateModel.setCity(affiliate.getCity().getName());
		affiliateModel.setCountry(affiliate.getCity().getCountry().getName());
		return affiliateModel;

	}
}
