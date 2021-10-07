package com.co2nsensus.co2mission.model.request;

import com.co2nsensus.co2mission.model.enums.ApplicationStatus;
import lombok.Data;

@Data
public class AffiliateApplicationUpdateRequestModel {
    
    private String id;
    private ApplicationStatus applicationStatus;    
    
}
