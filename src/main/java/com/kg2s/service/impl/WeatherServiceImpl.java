package com.kg2s.service.impl;

import com.kg2s.config.WeatherApiConfig;
import com.kg2s.domain.*;
import com.kg2s.repository.ApiKeyUsageRepository;
import com.kg2s.repository.WeatherDataRepository;
import com.kg2s.service.WeatherService;
import com.kg2s.service.OpenWeatherMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Yanfu Zhang
 * @date 2025-08-08
 * @description Weather Service Implementation
 */
@Service
public class WeatherServiceImpl implements WeatherService {
    
    @Autowired
    private WeatherDataRepository weatherDataRepository;
    
    @Autowired
    private ApiKeyUsageRepository apiKeyUsageRepository;
    
    @Autowired
    private WeatherApiConfig weatherApiConfig;
    
    @Autowired
    private OpenWeatherMapService openWeatherMapService;
    
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public WeatherInfoResp getWeatherInfo(String city, String country, String apiKey) {
        // Validate API key
        if (!isValidApiKey(apiKey)) {
            throw new IllegalArgumentException("Invalid API key");
        }
        
        // Check rate limit
        if (!checkRateLimit(apiKey)) {
            throw new RuntimeException("Hourly rate limit exceeded for API key: " + apiKey);
        }
        
        // Check if we have recent data in database (within last hour)
        Optional<WeatherData> existingData = weatherDataRepository.findLatestByCityAndCountry(city, country);
        if (existingData.isPresent() && isDataRecent(existingData.get())) {
            WeatherData data = existingData.get();
            return convertToResponse(data);
        }
        
        // Call OpenWeatherMap API to get real weather data
        OpenWeatherMapResponse openWeatherResponse = openWeatherMapService.getWeatherData(city, country);
        
        // Convert OpenWeatherMap response to our WeatherData entity
        WeatherData weatherData = convertOpenWeatherResponseToWeatherData(openWeatherResponse, city, country, apiKey);
        
        // Save to database
        weatherDataRepository.save(weatherData);
        
        // Update API key usage
        updateApiKeyUsage(apiKey);
        
        return convertToResponse(weatherData);
    }

    @Override
    public List<WeatherInfoResp> getWeatherHistory(String city, String country) {
        List<WeatherData> weatherDataList = weatherDataRepository.findByCityAndCountryOrderByTimestampDesc(city, country);
        return weatherDataList.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<WeatherInfoResp> getWeatherHistoryByApiKey(String apiKey) {
        List<WeatherData> weatherDataList = weatherDataRepository.findByApiKeyOrderByTimestampDesc(apiKey);
        return weatherDataList.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * Convert OpenWeatherMap API response to WeatherData entity
     */
    private WeatherData convertOpenWeatherResponseToWeatherData(OpenWeatherMapResponse response, String city, String country, String apiKey) {
        if (response.getWeather() == null || response.getWeather().isEmpty()) {
            throw new RuntimeException("No weather data received from OpenWeatherMap API");
        }
        
        OpenWeatherMapResponse.Weather weather = response.getWeather().get(0);
        
        return new WeatherData(
            city,
            country,
            apiKey,
            weather.getId(),
            weather.getMain(),
            weather.getDescription(),
            weather.getIcon()
        );
    }
    
    private boolean isValidApiKey(String apiKey) {
        return weatherApiConfig.getKeys() != null && weatherApiConfig.getKeys().contains(apiKey);
    }
    
    private boolean checkRateLimit(String apiKey) {
        Optional<ApiKeyUsage> usage = apiKeyUsageRepository.findByApiKey(apiKey);
        
        if (usage.isEmpty()) {
            return true; // First request
        }
        
        ApiKeyUsage apiKeyUsage = usage.get();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime hourStart = now.withMinute(0).withSecond(0).withNano(0);
        
        // If it's a new hour, reset the counter
        if (apiKeyUsage.getHourStart().isBefore(hourStart)) {
            apiKeyUsage.setRequestCount(0);
            apiKeyUsage.setHourStart(hourStart);
        }
        
        return apiKeyUsage.getRequestCount() < weatherApiConfig.getRateLimit().getRequestsPerHour();
    }
    
    private void updateApiKeyUsage(String apiKey) {
        Optional<ApiKeyUsage> usage = apiKeyUsageRepository.findByApiKey(apiKey);
        ApiKeyUsage apiKeyUsage;
        
        if (usage.isEmpty()) {
            apiKeyUsage = new ApiKeyUsage(apiKey);
        } else {
            apiKeyUsage = usage.get();
        }
        
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime hourStart = now.withMinute(0).withSecond(0).withNano(0);
        
        // If it's a new hour, reset the counter
        if (apiKeyUsage.getHourStart().isBefore(hourStart)) {
            apiKeyUsage.setRequestCount(0);
            apiKeyUsage.setHourStart(hourStart);
        }
        
        apiKeyUsage.setRequestCount(apiKeyUsage.getRequestCount() + 1);
        apiKeyUsage.setLastRequestTime(now);
        
        apiKeyUsageRepository.save(apiKeyUsage);
    }
    
    private boolean isDataRecent(WeatherData data) {
        LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
        return data.getTimestamp().isAfter(oneHourAgo);
    }
    
    private WeatherInfoResp convertToResponse(WeatherData data) {
        return new WeatherInfoResp(
            data.getWeatherId(),
            data.getMain(),
            data.getDescription(),
            data.getIcon(),
            data.getCity(),
            data.getCountry(),
            data.getApiKey(),
            data.getTimestamp().format(TIMESTAMP_FORMATTER)
        );
    }
} 