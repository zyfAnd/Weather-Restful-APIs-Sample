package com.kg2s.repository;

import com.kg2s.domain.ApiKeyUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Yanfu Zhang
 * @date 2025-08-08
 * @description API Key Usage Repository
 */
@Repository
public interface ApiKeyUsageRepository extends JpaRepository<ApiKeyUsage, Long> {
    
    /**
     * Find API key usage by API key
     */
    Optional<ApiKeyUsage> findByApiKey(String apiKey);
} 