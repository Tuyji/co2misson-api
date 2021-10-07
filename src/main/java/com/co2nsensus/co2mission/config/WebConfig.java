package com.co2nsensus.co2mission.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.co2nsensus.co2mission.interceptor.HeaderInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    
	@Autowired
	private HeaderInterceptor headerInterceptor; 
	
	@Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToEnumConverter());
    }
    
	@Override
    public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(headerInterceptor);
    }
	
	@Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*");
    }
}