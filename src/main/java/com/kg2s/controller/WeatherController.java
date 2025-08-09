package com.kg2s.controller;

import com.kg2s.domain.WeatherInfoResp;
import com.kg2s.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * @author Yanfu Zhang
 * @date 2025-08-08
 * @description Weather RESTful API Controller
 */
@RestController
@RequestMapping("/api/v1/weather")
@CrossOrigin(origins = "*")
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    /**
     * Get weather information by city and country using path variables
     * 
     * @param city City name
     * @param country Country code
     * @param apiKey API key for authentication and rate limiting
     * @return Weather information response
     */
    @GetMapping("/{city}/{country}")
    public ResponseEntity<WeatherInfoResp> getWeatherByPath(
            @PathVariable @NotBlank String city,
            @PathVariable @NotBlank String country,
            @RequestParam @NotBlank String apiKey) {
        
        WeatherInfoResp response = weatherService.getWeatherInfo(city, country, apiKey);
        return ResponseEntity.ok(response);
    }

    /**
     * Get weather information by city and country using query parameters
     * 
     * @param city City name
     * @param country Country code
     * @param apiKey API key for authentication and rate limiting
     * @return Weather information response
     */
    @GetMapping("/query")
    public ResponseEntity<WeatherInfoResp> getWeatherByQuery(
            @RequestParam @NotBlank String city,
            @RequestParam @NotBlank String country,
            @RequestParam @NotBlank String apiKey) {
        
        WeatherInfoResp response = weatherService.getWeatherInfo(city, country, apiKey);
        return ResponseEntity.ok(response);
    }

    /**
     * Get weather history by city and country
     * 
     * @param city City name
     * @param country Country code
     * @return List of weather information responses
     */
    @GetMapping("/history/{city}/{country}")
    public ResponseEntity<List<WeatherInfoResp>> getWeatherHistory(
            @PathVariable @NotBlank String city,
            @PathVariable @NotBlank String country) {
        
        List<WeatherInfoResp> response = weatherService.getWeatherHistory(city, country);
        return ResponseEntity.ok(response);
    }

    /**
     * Get weather history by API key
     * 
     * @param apiKey API key
     * @return List of weather information responses
     */
    @GetMapping("/history")
    public ResponseEntity<List<WeatherInfoResp>> getWeatherHistoryByApiKey(
            @RequestParam @NotBlank String apiKey) {
        
        List<WeatherInfoResp> response = weatherService.getWeatherHistoryByApiKey(apiKey);
        return ResponseEntity.ok(response);
    }

    /**
     * Health check endpoint
     * 
     * @return Health status
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("message", "Weather API is running");
        return ResponseEntity.ok(response);
    }
}
