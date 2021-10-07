package com.co2nsensus.co2mission.model.response.subscription;

import com.co2nsensus.co2mission.model.dto.AffiliateSubscriptionModel;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AffiliateSubscriptionsResponseModel {

    private List<AffiliateSubscriptionModel> affiliateSubscriptionModels = new ArrayList<>();
    private AffiliateSubscriptionsResponseMetrics affiliateSubscriptionsResponseMetrics;

}
