package com.co2nsensus.co2mission.redis;

import com.co2nsensus.co2mission.utils.DateUtil;

public class RedisKeyGenerator {

    private static final String DELIMETER = "#";

    public static String getKey(String afffiliateId,String key) {
        return afffiliateId + DELIMETER + key;
    }


    public static String generateMonthlySalesKey(String id) {
        StringBuilder sb = new StringBuilder();
        sb.append(id);
        sb.append(DELIMETER);
        sb.append(DateUtil.getCurrentYear());
        sb.append(DELIMETER);
        sb.append(DateUtil.getCurrentMonth());
        return sb.toString();
    }



    public static String generateAffiliateStatusKey(String id) {
        return id + DELIMETER + "STATUS";
    }



}
