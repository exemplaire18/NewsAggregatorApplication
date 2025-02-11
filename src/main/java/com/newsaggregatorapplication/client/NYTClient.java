package com.newsaggregatorapplication.client;

import com.newsaggregatorapplication.config.NewsConfig;
import com.newsaggregatorapplication.dto.ArticleDTO;
import com.newsaggregatorapplication.response.NYTResponse;
import com.newsaggregatorapplication.exception.ServiceUnavailableException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class NYTClient implements NewsClient {
    private final WebClient webClient;
    private final NewsConfig config;

    public NYTClient(@Qualifier("nytWebClient") WebClient webClient, NewsConfig config) {
        this.webClient = webClient;
        this.config = config;
    }

    @Override
    public List<ArticleDTO> fetchArticles(String keyword) {
        NYTResponse response;
        try {
            response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/svc/search/v2/articlesearch.json")
                            .queryParam("q", keyword)
                            .queryParam("api-key", config.getNytApiKey())
                            .build())
                    .retrieve()
                    .bodyToMono(NYTResponse.class)
                    .block();
        } catch (WebClientResponseException e) {
            throw new ServiceUnavailableException("NYT API unavailable");
        }

        assert response != null;
        return mapToCommonDTO(response.getResponse().getDocs(), "New York Times");
    }

    private List<ArticleDTO> mapToCommonDTO(List<NYTResponse.Doc> docs, String source) {
        return docs.stream()
                .map(doc -> ArticleDTO.builder()
                        .newsWebsite(source)
                        .headline(doc.getHeadline().getMain())
                        .url(doc.getWebUrl())
                        .description(doc.getSummary())
                        .publishedDate(doc.getPubDate())
                        .build())
                .collect(Collectors.toList());
    }
}