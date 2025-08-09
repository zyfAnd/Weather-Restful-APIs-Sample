package com.kg2s.service;

import com.kg2s.domain.OpenWeatherMapResponse;

/**
 * @author Yanfu Zhang
 * @date 2025-08-08
 * @description OpenWeatherMap Service Interface
 */
public interface OpenWeatherMapService {
    
    /**
     * Get weather data from OpenWeatherMap API
     * 
     * @param city City name
     * @param country Country code
     * @return OpenWeatherMap API response
     */
    OpenWeatherMapResponse getWeatherData(String city, String country);
} 