package com.newsaggregatorapplication.dto;

import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class ResponseDTO {
    // Search metadata
    private String searchKeyword;
    private String city; // Optional (if supported by future filters)
    private long timeTakenMs; // Time to process the request

    // Pagination
    private int currentPage;
    private int totalPages;
    private Integer previousPageNo;
    private Integer nextPageNo;

    // Articles (deduplicated and paginated)
    private List<ArticleDTO> articles;

    // HATEOAS links
    private Map<String, String> links;

    // Helper method to add pagination links
    public void addLink(String rel, String href) {
        if (links == null) links = new HashMap<>();
        links.put(rel, href);
    }
}
