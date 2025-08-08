package com.kg2s.service;

import com.kg2s.domain.WeatherInfoResp;
import java.util.List;

/**
 * @author Yanfu Zhang
 * @date 2025-08-08
 * @description Weather Service Interface
 */
public interface WeatherService {
    
    /**
     * Get weather information by city and country
     */
    WeatherInfoResp getWeatherInfo(String city, String country, String apiKey);
    
    /**
     * Get weather history by city and country
     */
    List<WeatherInfoResp> getWeatherHistory(String city, String country);
    
    /**
     * Get weather history by API key
     */
    List<WeatherInfoResp> getWeatherHistoryByApiKey(String apiKey);
} 