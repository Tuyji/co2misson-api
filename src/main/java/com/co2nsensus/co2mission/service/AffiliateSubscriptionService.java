package com.co2nsensus.co2mission.service;

import com.co2nsensus.co2mission.model.dto.SubscriptionUpdateMessage;
import com.co2nsensus.co2mission.model.response.subscription.AffiliateSubscriptionsResponseModel;

public interface AffiliateSubscriptionService {

	void updateAffiliateSubscription(SubscriptionUpdateMessage subscriptionUpdateMessage);

	AffiliateSubscriptionsResponseModel getAffiliateSubscribers(String id, String source);

}
