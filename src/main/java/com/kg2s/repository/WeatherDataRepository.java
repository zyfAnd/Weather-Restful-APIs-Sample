package com.kg2s.repository;

import com.kg2s.domain.WeatherData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Yanfu Zhang
 * @date 2025-08-08
 * @description Weather Data Repository
 */
@Repository
public interface WeatherDataRepository extends JpaRepository<WeatherData, Long> {
    
    /**
     * Find weather data by city and country
     */
    List<WeatherData> findByCityAndCountryOrderByTimestampDesc(String city, String country);
    
    /**
     * Find weather data by API key
     */
    List<WeatherData> findByApiKeyOrderByTimestampDesc(String apiKey);
    
    /**
     * Find latest weather data by city and country
     */
    @Query("SELECT w FROM WeatherData w WHERE w.city = :city AND w.country = :country " +
           "ORDER BY w.timestamp DESC LIMIT 1")
    Optional<WeatherData> findLatestByCityAndCountry(@Param("city") String city, 
                                                    @Param("country") String country);
    
    /**
     * Find weather data by city, country and API key
     */
    List<WeatherData> findByCityAndCountryAndApiKeyOrderByTimestampDesc(String city, 
                                                                       String country, 
                                                                       String apiKey);
} 