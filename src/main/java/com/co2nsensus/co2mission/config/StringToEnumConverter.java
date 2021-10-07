package com.co2nsensus.co2mission.config;

import com.co2nsensus.co2mission.model.enums.ApplicationStatus;
import org.springframework.core.convert.converter.Converter;

public class StringToEnumConverter implements Converter<String, ApplicationStatus> {

    @Override
    public ApplicationStatus convert(String source) {
        try {
            return ApplicationStatus.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
