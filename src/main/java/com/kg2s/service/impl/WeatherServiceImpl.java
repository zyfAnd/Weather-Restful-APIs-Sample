package com.kg2s.service.impl;

import com.kg2s.config.WeatherApiConfig;
import com.kg2s.domain.*;
import com.kg2s.repository.ApiKeyUsageRepository;
import com.kg2s.repository.WeatherDataRepository;
import com.kg2s.service.WeatherService;
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
        
        // Generate mock weather data based on city (simulating OpenWeatherMap API call)
        WeatherData mockWeatherData = generateMockWeatherData(city, country, apiKey);
        
        // Save to database
        weatherDataRepository.save(mockWeatherData);
        
        // Update API key usage
        updateApiKeyUsage(apiKey);
        
        return convertToResponse(mockWeatherData);
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
     * Generate mock weather data to simulate OpenWeatherMap API response
     */
    private WeatherData generateMockWeatherData(String city, String country, String apiKey) {
        String description;
        String main;
        String weatherId;
        String icon;
        
        // Generate different weather based on city name for variety
        switch (city.toLowerCase()) {
            case "london":
                description = "light rain";
                main = "Rain";
                weatherId = "500";
                icon = "10d";
                break;
            case "paris":
                description = "few clouds";
                main = "Clouds";
                weatherId = "801";
                icon = "02d";
                break;
            case "tokyo":
                description = "clear sky";
                main = "Clear";
                weatherId = "800";
                icon = "01d";
                break;
            case "new york":
            case "newyork":
                description = "scattered clouds";
                main = "Clouds";
                weatherId = "802";
                icon = "03d";
                break;
            case "beijing":
                description = "haze";
                main = "Haze";
                weatherId = "721";
                icon = "50d";
                break;
            default:
                description = "partly cloudy";
                main = "Clouds";
                weatherId = "803";
                icon = "04d";
                break;
        }
        
        return new WeatherData(city, country, apiKey, weatherId, main, description, icon);
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