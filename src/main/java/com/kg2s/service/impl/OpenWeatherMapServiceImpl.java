package com.kg2s.service.impl;

import com.kg2s.config.OpenWeatherMapConfig;
import com.kg2s.domain.OpenWeatherMapResponse;
import com.kg2s.service.OpenWeatherMapService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

/**
 * @author Yanfu Zhang
 * @date 2025-08-08
 * @description OpenWeatherMap Service Implementation using RestTemplate
 */
@Service
public class OpenWeatherMapServiceImpl implements OpenWeatherMapService {
    
    private static final Logger logger = LoggerFactory.getLogger(OpenWeatherMapServiceImpl.class);
    
    @Autowired
    private OpenWeatherMapConfig openWeatherMapConfig;
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Override
    public OpenWeatherMapResponse getWeatherData(String city, String country) {
        try {
            String url = buildApiUrl(city, country);
            logger.info("Calling OpenWeatherMap API: {}", url);
            
            ResponseEntity<OpenWeatherMapResponse> response = restTemplate.getForEntity(
                url, OpenWeatherMapResponse.class);
            
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                logger.info("Successfully retrieved weather data for {}/{}", city, country);
                return response.getBody();
            } else {
                logger.error("Failed to get weather data. Status: {}", response.getStatusCode());
                throw new RuntimeException("Failed to get weather data from OpenWeatherMap API");
            }
            
        } catch (HttpClientErrorException e) {
            logger.error("HTTP error when calling OpenWeatherMap API: {}", e.getMessage());
            throw new RuntimeException("Error calling OpenWeatherMap API: " + e.getMessage());
        } catch (ResourceAccessException e) {
            logger.error("Network error when calling OpenWeatherMap API: {}", e.getMessage());
            throw new RuntimeException("Network error when calling OpenWeatherMap API: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error when calling OpenWeatherMap API: {}", e.getMessage());
            throw new RuntimeException("Unexpected error when calling OpenWeatherMap API: " + e.getMessage());
        }
    }
    
    private String buildApiUrl(String city, String country) {
        String baseUrl = openWeatherMapConfig.getBaseUrl();
        String apiKey = openWeatherMapConfig.getKey();
        String units = openWeatherMapConfig.getUnits();
        
        // Format: http://api.openweathermap.org/data/2.5/weather?q={city},{country}&appid={apiKey}&units={units}
        return String.format("%s/weather?q=%s,%s&appid=%s&units=%s", 
            baseUrl, city, country, apiKey, units);
    }
} 