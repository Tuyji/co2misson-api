package com.co2nsensus.co2mission.service.co2nsenus;

import com.co2nsensus.co2mission.service.co2nsenus.exception.ChannelCreationException;
import com.co2nsensus.co2mission.service.co2nsenus.types.CreateChannelRequest;
import com.co2nsensus.co2mission.service.co2nsenus.types.CreateChannelResponse;
import com.co2nsensus.co2mission.service.co2nsenus.types.CreateWidgetRequest;
import com.co2nsensus.co2mission.service.co2nsenus.types.CreateWidgetResponse;

public interface Co2nsensusInterface {
	CreateChannelResponse createChannel(CreateChannelRequest createChannelRequest) throws ChannelCreationException;
	
	CreateWidgetResponse createWidget(CreateWidgetRequest getWidgetRequest) throws ChannelCreationException;
}
