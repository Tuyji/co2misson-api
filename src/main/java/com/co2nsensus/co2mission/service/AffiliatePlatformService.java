package com.co2nsensus.co2mission.service;

import java.util.List;

import com.co2nsensus.co2mission.model.entity.platform.AffiliatePlatform;
import com.co2nsensus.co2mission.model.response.platform.AffiliatePlatformModel;
import com.co2nsensus.co2mission.model.response.platform.AffiliatePlatformModelList;
import com.co2nsensus.co2mission.model.response.platform.PlatformModel;

public interface AffiliatePlatformService {

    AffiliatePlatformModelList getPlatformsByAffiliateId(String affiliateId);
    
    AffiliatePlatformModel addPlatform(String affiliateId,AffiliatePlatformModel affiliatePlatformModel);
    
    AffiliatePlatform getAffiliatePlatform(String affiliatePlatformId);
    
    void deletePlatform(String affiliateId,String affiliatePlatformId);
    
    List<PlatformModel> getPlatforms();
    
}
