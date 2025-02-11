package com.newsaggregatorapplication.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "news")
@Data
public class NewsConfig {
    private String guardianApiKey;
    private String nytApiKey;
    private int defaultPageSize;
    private boolean offlineMode;
}