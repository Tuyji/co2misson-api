package com.co2nsensus.co2mission.model.dto.logging;


import java.util.UUID;

import org.springframework.stereotype.Component;

public class Correlation {
    
    public static String getNewCorrelationId() {
    	return  "CO2MISSION" + System.currentTimeMillis() + "" +  UUID.randomUUID().toString().replace("-", "");
    }
}
