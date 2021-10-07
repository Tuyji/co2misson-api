package com.co2nsensus.co2mission.model.dto;

import java.util.List;

import com.co2nsensus.co2mission.model.entity.Affiliate;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AffiliateEntityListPagedModel {
	private List<Affiliate> affiliateList;
	private int totalPages;
    private int pageNumber;
    private int pageSize;
}
 