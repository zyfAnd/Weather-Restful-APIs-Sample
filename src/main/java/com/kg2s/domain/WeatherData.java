package com.kg2s.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Yanfu Zhang
 * @date 2025-08-08
 * @description Weather Data Entity for H2 Database
 */
@Entity
@Table(name = "weather_data")
public class WeatherData {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String city;
    
    @Column(nullable = false)
    private String country;
    
    @Column(nullable = false)
    private String apiKey;
    
    @Column(nullable = false)
    private String weatherId;
    
    @Column(nullable = false)
    private String main;
    
    @Column(nullable = false)
    private String description;
    
    @Column(nullable = false)
    private String icon;
    
    @Column(nullable = false)
    private LocalDateTime timestamp;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;

    public WeatherData() {
        this.createdAt = LocalDateTime.now();
    }

    public WeatherData(String city, String country, String apiKey, String weatherId, 
                      String main, String description, String icon) {
        this();
        this.city = city;
        this.country = country;
        this.apiKey = apiKey;
        this.weatherId = weatherId;
        this.main = main;
        this.description = description;
        this.icon = icon;
        this.timestamp = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
} 