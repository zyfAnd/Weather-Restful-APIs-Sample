package com.kg2s.service.impl;

import com.kg2s.client.OpenWeatherMapClient;
import com.kg2s.domain.*;
import com.kg2s.repository.ApiKeyUsageRepository;
import com.kg2s.repository.WeatherDataRepository;
import com.kg2s.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    private OpenWeatherMapClient openWeatherMapClient;
    
    @Autowired
    private WeatherDataRepository weatherDataRepository;
    
    @Autowired
    private ApiKeyUsageRepository apiKeyUsageRepository;
    
    @Value("${openweathermap.api.key}")
    private String openWeatherMapApiKey;
    
    @Value("${openweathermap.api.units}")
    private String units;
    
    @Value("${api.rate-limit.requests-per-hour}")
    private Integer requestsPerHour;
    
    @Value("${api.keys}")
    private List<String> validApiKeys;
    
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
        
        // Call OpenWeatherMap API
        try {
            String query = city + "," + country;
            OpenWeatherMapResponse response = openWeatherMapClient.getWeatherByCity(query, openWeatherMapApiKey, units);
            
            if (response.getCod() != null && response.getCod() == 200 && response.getWeather() != null && !response.getWeather().isEmpty()) {
                OpenWeatherMapResponse.Weather weather = response.getWeather().get(0);
                
                // Save to database
                WeatherData weatherData = new WeatherData(
                    city, country, apiKey, weather.getId(), 
                    weather.getMain(), weather.getDescription(), weather.getIcon()
                );
                weatherDataRepository.save(weatherData);
                
                // Update API key usage
                updateApiKeyUsage(apiKey);
                
                return convertToResponse(weatherData);
            } else {
                throw new RuntimeException("Failed to get weather data from OpenWeatherMap API: " + response.getMessage());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error calling OpenWeatherMap API: " + e.getMessage());
        }
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
    
    private boolean isValidApiKey(String apiKey) {
        return validApiKeys.contains(apiKey);
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
        
        return apiKeyUsage.getRequestCount() < requestsPerHour;
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