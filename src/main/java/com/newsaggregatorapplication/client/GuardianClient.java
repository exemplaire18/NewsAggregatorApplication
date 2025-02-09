package com.newsaggregatorapplication.client;

import com.newsaggregatorapplication.config.NewsConfig;
import com.newsaggregatorapplication.dto.ArticleDTO;
import com.newsaggregatorapplication.response.GuardianResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class GuardianClient implements NewsClient {

    private final WebClient webClient;
    private final NewsConfig config;

    public GuardianClient(@Qualifier("guardianWebClient") WebClient webClient, NewsConfig config) {
        this.webClient = webClient;
        this.config = config;
    }

    @Override
    public List<ArticleDTO> fetchArticles(String keyword, Integer page, Integer size) {
        // Apply defaults if pagination params are not provided
        int pageNumber = (page != null) ? page : 1;
        int pageSize = (size != null) ? size : config.getDefaultPageSize();

        GuardianResponse response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search")
                        .queryParam("q", keyword)
                        .queryParam("page", pageNumber)
                        .queryParam("page-size", pageSize)
                        .queryParam("api-key", config.getGuardianApiKey())
                        .build())
                .retrieve()
                .bodyToMono(GuardianResponse.class)
                .block();

        assert response != null;
        return mapToCommonDTO(response.getResponse().getResults(), "Guardian");
    }

    private List<ArticleDTO> mapToCommonDTO(List<GuardianResponse.Result> results, String source) {
        return results.stream()
                .map(result -> ArticleDTO.builder()
                        .newsWebsite(source)
                        .headline(result.getWebTitle())
                        .url(result.getWebUrl())
                        .description(result.getSectionName())
                        .publishedDate(result.getWebPublicationDate())
                        .build())
                .collect(Collectors.toList());
    }
}