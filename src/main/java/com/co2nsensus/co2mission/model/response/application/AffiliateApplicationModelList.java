package com.co2nsensus.co2mission.model.response.application;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AffiliateApplicationModelList {

    private List<AffiliateApplicationModel> affiliateApplicationModels = new ArrayList<>();
}
