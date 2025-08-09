package com.kg2s.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Yanfu Zhang
 * @date 2025-08-08
 * @description OpenWeatherMap Configuration
 */
@Configuration
@ConfigurationProperties(prefix = "openweathermap.api")
public class OpenWeatherMapConfig {
    
    private String key;
    private String baseUrl;
    private String units;
    
    public String getKey() {
        return key;
    }
    
    public void setKey(String key) {
        this.key = key;
    }
    
    public String getBaseUrl() {
        return baseUrl;
    }
    
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
    
    public String getUnits() {
        return units;
    }
    
    public void setUnits(String units) {
        this.units = units;
    }
} 