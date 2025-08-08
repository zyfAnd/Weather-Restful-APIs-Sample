package com.kg2s.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author Yanfu Zhang
 * @date 2025-08-08
 * @description Weather API Configuration
 */
@Configuration
@ConfigurationProperties(prefix = "api")
public class WeatherApiConfig {
    
    private List<String> keys;
    private RateLimit rateLimit;
    
    public static class RateLimit {
        private Integer requestsPerHour;
        
        public Integer getRequestsPerHour() {
            return requestsPerHour;
        }
        
        public void setRequestsPerHour(Integer requestsPerHour) {
            this.requestsPerHour = requestsPerHour;
        }
    }
    
    public List<String> getKeys() {
        return keys;
    }
    
    public void setKeys(List<String> keys) {
        this.keys = keys;
    }
    
    public RateLimit getRateLimit() {
        return rateLimit;
    }
    
    public void setRateLimit(RateLimit rateLimit) {
        this.rateLimit = rateLimit;
    }
} 