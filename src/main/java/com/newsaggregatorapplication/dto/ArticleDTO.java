package com.newsaggregatorapplication.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ArticleDTO {
    private String newsWebsite; // "Guardian" or "New York Times"
    private String url;          // Article URL
    private String headline;     // Title/headline
    private String description;  // Summary/abstract
    private String publishedDate;
}