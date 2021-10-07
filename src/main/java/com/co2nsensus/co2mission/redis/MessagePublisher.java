package com.co2nsensus.co2mission.redis;

public interface MessagePublisher {

    void publish(final String message);
}