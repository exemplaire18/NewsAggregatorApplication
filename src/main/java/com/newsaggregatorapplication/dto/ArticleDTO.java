package com.newsaggregatorapplication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "Represents a news article")
@Data
@Builder
public class ArticleDTO {
    @Schema(description = "Source website", example = "Guardian")
    private String newsWebsite;

    @Schema(description = "Article URL", example = "https://guardian.com/apple-news")
    private String url;

    @Schema(description = "Headline of the article", example = "Apple Launches New iPhone")
    private String headline;

    @Schema(description = "Brief summary of the article")
    private String description;

    @Schema(description = "Published Date of the article")
    private String publishedDate;
}