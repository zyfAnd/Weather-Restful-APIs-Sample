package com.kg2s.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Yanfu Zhang
 * @date 2025-08-08
 * @description Weather Information Response DTO
 */
public class WeatherInfoResp {
    private String id;
    private String main;
    private String description;
    private String icon;
    private String city;
    private String country;
    private String apiKey;
    private String timestamp;

    public WeatherInfoResp() {}

    public WeatherInfoResp(String id, String main, String description, String icon, 
                          String city, String country, String apiKey, String timestamp) {
        this.id = id;
        this.main = main;
        this.description = description;
        this.icon = icon;
        this.city = city;
        this.country = country;
        this.apiKey = apiKey;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
