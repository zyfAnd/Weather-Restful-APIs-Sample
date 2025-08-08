package com.kg2s.client;

import com.kg2s.domain.OpenWeatherMapResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Yanfu Zhang
 * @date 2025-08-08
 * @description OpenWeatherMap API Client
 */
@FeignClient(name = "openweathermap", url = "${openweathermap.api.base-url}")
public interface OpenWeatherMapClient {
    
    /**
     * Get weather data by city name and country code
     */
    @GetMapping("/weather")
    OpenWeatherMapResponse getWeatherByCity(
        @RequestParam("q") String query,
        @RequestParam("appid") String apiKey,
        @RequestParam("units") String units
    );
} 