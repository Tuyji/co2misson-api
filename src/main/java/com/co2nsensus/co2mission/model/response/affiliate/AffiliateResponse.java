package com.co2nsensus.co2mission.model.response.affiliate;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AffiliateResponse {

    private List<AffiliateModel> affiliateModels;
    private int totalPages;
    private int pageNumber;
    private int pageSize;
}
