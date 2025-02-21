package com.newsaggregatorapplication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Schema(description = "Response containing aggregated news articles")
@Data
@Builder
public class ResponseDTO {
    @Schema(description = "Search keyword used", example = "apple")
    private String searchKeyword;
    @Schema(description = "Time taken to fetch the articles based on the keyword")
    private long timeTakenMs;
    @Schema(description = "List of articles (deduplicated)")
    private List<ArticleDTO> articles;
    @Schema(description = "Current page number", example = "1")
    private int currentPage;
    @Schema(description = "Total pages available", example = "5")
    private int totalPages;
    @Schema(description = "Links for previous/next pages")
    private Map<String, String> links;

    public void addLink(String rel, String href) {
        if (links == null) links = new HashMap<>();
        links.put(rel, href);
    }
}
