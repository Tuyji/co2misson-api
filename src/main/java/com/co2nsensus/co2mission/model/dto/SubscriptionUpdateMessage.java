package com.co2nsensus.co2mission.model.dto;

import com.co2nsensus.co2mission.model.enums.SubscriptionMessageActionType;

import lombok.Data;

@Data
public class SubscriptionUpdateMessage extends SubscriptionModel{
	private SubscriptionMessageActionType action;
}
