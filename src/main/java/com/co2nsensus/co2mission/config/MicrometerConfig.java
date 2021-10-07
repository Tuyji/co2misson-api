package com.co2nsensus.co2mission.config;

import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.config.MeterFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MicrometerConfig {

    @Bean
    public TimedAspect timedAspect(MeterRegistry registry) {
        return new TimedAspect(registry);
    }

    // Added for testing purposes only
    @Bean
    public MeterFilter excludeTomcatFilter() {
        return MeterFilter.denyNameStartsWith("tomcat");
    }
}
