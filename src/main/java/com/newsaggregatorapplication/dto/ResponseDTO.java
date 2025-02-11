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
    private String city; // Optional (if supported by future filters)
    private long timeTakenMs; // Time to process the request

    @Schema(description = "Current page number", example = "1")
    private int currentPage;
    @Schema(description = "Total pages available", example = "5")
    private int totalPages;
    private Integer previousPageNo;
    private Integer nextPageNo;

    @Schema(description = "List of articles (deduplicated)")
    private List<ArticleDTO> articles;

    // HATEOAS links
    private Map<String, String> links;

    // Helper method to add pagination links
    public void addLink(String rel, String href) {
        if (links == null) links = new HashMap<>();
        links.put(rel, href);
    }
}
