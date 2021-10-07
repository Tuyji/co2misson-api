package com.co2nsensus.co2mission.redis;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.co2nsensus.co2mission.logging.Loggable;
import com.co2nsensus.co2mission.model.dto.AffiliateSubscriptionModel;
import com.co2nsensus.co2mission.model.dto.SubscriptionUpdateMessage;
import com.co2nsensus.co2mission.service.AffiliateSubscriptionService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Loggable
@Slf4j
public class SubscriptionMessageSubscriber {

	private AffiliateSubscriptionService affiliateSubscriptionService;
	private ObjectMapper objectMapper;

	public SubscriptionMessageSubscriber() {
	}

	@Autowired
	public SubscriptionMessageSubscriber(AffiliateSubscriptionService affiliateSubscriptionService,
			ObjectMapper objectMapper) {
		this.affiliateSubscriptionService = affiliateSubscriptionService;
		this.objectMapper = objectMapper;
	}

	public void onUpdated(String message) {
		try {
			SubscriptionUpdateMessage subscriptionUpdateMessage = objectMapper.readValue(message,
					SubscriptionUpdateMessage.class);

			affiliateSubscriptionService.updateAffiliateSubscription(subscriptionUpdateMessage);

		} catch (IOException e) {
			log.error("REDIS ERR: Subscription message subscriber error on update : ", e);
		} catch (Exception ex) {
			log.error("REDIS ERR: Subscription message subscriber error on update : ", ex);
		}
	}

}