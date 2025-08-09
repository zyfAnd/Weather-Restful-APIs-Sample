package com.kg2s.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Yanfu Zhang
 * @date 2025-08-08
 * @description API Key Usage Entity for rate limiting
 */
@Entity
@Table(name = "api_key_usage")
public class ApiKeyUsage {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String apiKey;
    
    @Column(nullable = false)
    private Integer requestCount;
    
    @Column(nullable = false)
    private LocalDateTime lastRequestTime;
    
    @Column(nullable = false)
    private LocalDateTime hourStart;

    public ApiKeyUsage() {}

    public ApiKeyUsage(String apiKey) {
        this.apiKey = apiKey;
        this.requestCount = 0;
        this.lastRequestTime = LocalDateTime.now();
        this.hourStart = LocalDateTime.now().withMinute(0).withSecond(0).withNano(0);
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public Integer getRequestCount() {
        return requestCount;
    }

    public void setRequestCount(Integer requestCount) {
        this.requestCount = requestCount;
    }

    public LocalDateTime getLastRequestTime() {
        return lastRequestTime;
    }

    public void setLastRequestTime(LocalDateTime lastRequestTime) {
        this.lastRequestTime = lastRequestTime;
    }

    public LocalDateTime getHourStart() {
        return hourStart;
    }

    public void setHourStart(LocalDateTime hourStart) {
        this.hourStart = hourStart;
    }
} 