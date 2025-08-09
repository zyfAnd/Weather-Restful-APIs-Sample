package com.kg2s.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * @author Yanfu Zhang
 * @date 2025-08-08
 * @description OpenWeatherMap API Response DTO
 */
public class OpenWeatherMapResponse {
    
    @JsonProperty("weather")
    private List<Weather> weather;
    
    @JsonProperty("main")
    private Main main;
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("cod")
    private Integer cod;
    
    @JsonProperty("message")
    private String message;
    
    public static class Weather {
        @JsonProperty("id")
        private String id;
        
        @JsonProperty("main")
        private String main;
        
        @JsonProperty("description")
        private String description;
        
        @JsonProperty("icon")
        private String icon;
        
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
    }
    
    public static class Main {
        @JsonProperty("temp")
        private Double temp;
        
        @JsonProperty("feels_like")
        private Double feelsLike;
        
        @JsonProperty("temp_min")
        private Double tempMin;
        
        @JsonProperty("temp_max")
        private Double tempMax;
        
        @JsonProperty("pressure")
        private Integer pressure;
        
        @JsonProperty("humidity")
        private Integer humidity;
        
        // Getters and Setters
        public Double getTemp() {
            return temp;
        }
        
        public void setTemp(Double temp) {
            this.temp = temp;
        }
        
        public Double getFeelsLike() {
            return feelsLike;
        }
        
        public void setFeelsLike(Double feelsLike) {
            this.feelsLike = feelsLike;
        }
        
        public Double getTempMin() {
            return tempMin;
        }
        
        public void setTempMin(Double tempMin) {
            this.tempMin = tempMin;
        }
        
        public Double getTempMax() {
            return tempMax;
        }
        
        public void setTempMax(Double tempMax) {
            this.tempMax = tempMax;
        }
        
        public Integer getPressure() {
            return pressure;
        }
        
        public void setPressure(Integer pressure) {
            this.pressure = pressure;
        }
        
        public Integer getHumidity() {
            return humidity;
        }
        
        public void setHumidity(Integer humidity) {
            this.humidity = humidity;
        }
    }
    
    // Getters and Setters
    public List<Weather> getWeather() {
        return weather;
    }
    
    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }
    
    public Main getMain() {
        return main;
    }
    
    public void setMain(Main main) {
        this.main = main;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Integer getCod() {
        return cod;
    }
    
    public void setCod(Integer cod) {
        this.cod = cod;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
} 