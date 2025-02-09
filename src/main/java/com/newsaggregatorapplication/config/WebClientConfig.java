package com.newsaggregatorapplication.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${news.guardian-base-url}")
    private String guardianBaseUrl;

    @Value("${news.nyt-base-url}")
    private String nytBaseUrl;

    @Bean("guardianWebClient")
    public WebClient guardianWebClient() {
        return WebClient.builder()
                .baseUrl(guardianBaseUrl)
                .build();
    }

    @Bean("nytWebClient")
    public WebClient nytWebClient() {
        return WebClient.builder()
                .baseUrl(nytBaseUrl)
                .build();
    }
}